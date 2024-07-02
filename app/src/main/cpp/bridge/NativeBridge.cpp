// Created by Denys on 04.07.2023.

#include <pthread.h>
#include <unistd.h>
#include "NativeBridge.h"
#include "NativeBridgeConstants.h"
#include "../mod/data/GlobalSettings.h"
#include "../mod/data/table/TableProperties.h"
#include "../mod/memory/MemoryManager.h"
#include "../mod/predictor/Prediction.h"
#include "../utils/logger.h"

const char *NativeBridge::TAG = "jni_NativeBridge";
bool NativeBridge::isShouldRunThread = true;
JavaVM *NativeBridge::mJvm = nullptr;
JNIEnv *NativeBridge::mEnv = nullptr;
jobject NativeBridge::mNativeRepository = nullptr;
jmethodID NativeBridge::mUpdateShotResult = nullptr;
jfloatArray NativeBridge::mEmptyShotResult = nullptr;

void *NativeBridge::cuePropertiesThread(void *) {
    while (NativeBridge::isShouldRunThread) {
        MemoryManager::CueProperties::writeCuePropertiesToMemory();
    }
    return nullptr;
}

void *NativeBridge::predictorThread(void *) {
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6;
    args.name = "predictorThread";
    args.group = nullptr;
    int attachResult = mJvm->AttachCurrentThread(&mEnv, &args);
    if (attachResult != JNI_OK) {
        LOGE(TAG, "Could not attach predictorThread");
        return nullptr;
    }
    NativeBridge::initEmptyShotResult();
    bool isShouldRedraw;
    if (GlobalSettings::IS_DEBUG) {
        LOGD(TAG, "DEBUG build");
        while (NativeBridge::isShouldRunThread) {
            isShouldRedraw = gPrediction->mockPredictShotResult();
            if (isShouldRedraw) {
                NativeBridge::updateShotResult(
                        gPrediction->getShotResult(),
                        gPrediction->getShotResultSize()
                );
            }
        }
    } else {
        LOGD(TAG, "RELEASE build");
        if (!MemoryManager::initialize()) {
            LOGD(TAG, "Could not initialize MemoryManager");
            return nullptr;
        }
        pthread_t thread;
        pthread_create(&thread, nullptr, cuePropertiesThread, nullptr);
        while (NativeBridge::isShouldRunThread) {
            if (GlobalSettings::isDrawLinesEnabled || GlobalSettings::isDrawShotStateEnabled) {
                isShouldRedraw = gPrediction->determineShotResult();
                if (isShouldRedraw) {
                    NativeBridge::updateShotResult(
                            gPrediction->getShotResult(),
                            gPrediction->getShotResultSize()
                    );
                }
            } else {
                NativeBridge::clearShotResult();
                sleep(1);
            }
        }
    }
    releaseGlobalRefs(mEnv);
    mJvm->DetachCurrentThread();
    LOGD(TAG, "Exiting predictorThread()");
    return nullptr;
}

void NativeBridge::initEmptyShotResult() {
    mEmptyShotResult = mEnv->NewFloatArray(2);
    jfloat initialValues[2] = {0.0f, 0.0f};
    mEnv->SetFloatArrayRegion(mEmptyShotResult, 0, 2, initialValues);
    mEmptyShotResult = (jfloatArray) mEnv->NewGlobalRef(mEmptyShotResult);
}

void NativeBridge::updateAimSettings(
        JNIEnv *,
        jclass,
        jboolean drawLinesEnabled,
        jboolean drawShotStateEnabled,
        jboolean drawOpponentsLinesEnabled,
        jboolean preciseTrajectoriesEnabled,
        jint cuePower,
        jint cueSpin
) {
    GlobalSettings::isDrawLinesEnabled = drawLinesEnabled;
    GlobalSettings::isDrawShotStateEnabled = drawShotStateEnabled;
    GlobalSettings::isDrawOpponentsLinesEnabled = drawOpponentsLinesEnabled;
    GlobalSettings::isPreciseTrajectoriesEnabled = preciseTrajectoriesEnabled;
    GlobalSettings::cuePower = cuePower;
    GlobalSettings::cueSpin = cueSpin;
}

void NativeBridge::setTablePosition(JNIEnv *, jclass, jfloat left, jfloat, jfloat right, jfloat bottom) {
    Point2D::setTableData(left, right, bottom);
    TableProperties::initializePocketPositionsInScreen();
}

void NativeBridge::setNativeRepository(JNIEnv *env, jclass, jobject nativeRepository) {
    env->GetJavaVM(&mJvm);
    mNativeRepository = env->NewGlobalRef(nativeRepository);
    if (setUpdateShotResultMethodId(env) != JNI_OK) {
        releaseGlobalRefs(env);
        return;
    }
    pthread_t thread;
    pthread_create(&thread, nullptr, predictorThread, nullptr);
}

int NativeBridge::setUpdateShotResultMethodId(JNIEnv *env) {
    jclass nativeRepositoryClass = env->GetObjectClass(mNativeRepository);
    if (!nativeRepositoryClass) {
        LOGE(TAG, "Could not find NativeRepository class");
        return JNI_ERR;
    }
    mUpdateShotResult = env->GetMethodID(
            nativeRepositoryClass,
            METHOD_UPDATE_SHOT_RESULT,
            SIG_UPDATE_SHOT_RESULT
    );
    if (!mUpdateShotResult) {
        LOGE(TAG, "Could not find updateShotResult method in the NativeRepository class");
        return JNI_ERR;
    }
    return JNI_OK;
}

void NativeBridge::updateShotResult(float *shotResult, int size) {
    jfloatArray jShotResult = mEnv->NewFloatArray(size);
    mEnv->SetFloatArrayRegion(jShotResult, 0, size, &(shotResult[0]));
    mEnv->CallVoidMethod(mNativeRepository, mUpdateShotResult, jShotResult);
    mEnv->DeleteLocalRef(jShotResult);
}

void NativeBridge::clearShotResult() {
    mEnv->CallVoidMethod(mNativeRepository, mUpdateShotResult, mEmptyShotResult);
}

void NativeBridge::releaseGlobalRefs(JNIEnv *env) {
    if (mNativeRepository != nullptr) {
        env->DeleteGlobalRef(mNativeRepository);
        mNativeRepository = nullptr;
    }
    if (mEmptyShotResult != nullptr) {
        env->DeleteGlobalRef(mEmptyShotResult);
        mEmptyShotResult = nullptr;
    }
}

void NativeBridge::exitThread(JNIEnv *, jclass) {
    isShouldRunThread = false;
}

int NativeBridge::registerNativeMethods(JNIEnv *env) {
    if (env == nullptr) {
        LOGE(TAG, "JNIEnv pointer is nullptr");
        return JNI_ERR;
    }
    jclass kotlinNativeBridgeClass = env->FindClass(CLASS_NATIVE_BRIDGE_KOTLIN);
    if (!kotlinNativeBridgeClass) {
        LOGE(TAG, "Could not find NativeBridge class in Kotlin");
        return JNI_ERR;
    }
    JNINativeMethod methods[] = {
            {METHOD_UPDATE_AIM_SETTINGS,   SIG_UPDATE_AIM_SETTINGS,   reinterpret_cast<void *>(NativeBridge::updateAimSettings)},
            {METHOD_EXIT_THREAD,           SIG_EXIT_THREAD,           reinterpret_cast<void *>(NativeBridge::exitThread)},
            {METHOD_SET_NATIVE_REPOSITORY, SIG_SET_NATIVE_REPOSITORY, reinterpret_cast<void *>(NativeBridge::setNativeRepository)},
            {METHOD_SET_TABLE_POSITION,    SIG_SET_TABLE_POSITION,    reinterpret_cast<void *>(NativeBridge::setTablePosition)},
    };
    int nMethods = (sizeof(methods) / sizeof(methods[0]));
    int registrationResult = env->RegisterNatives(kotlinNativeBridgeClass, methods, nMethods);
    if (registrationResult != JNI_OK) {
        LOGE(TAG, "Could not register native methods");
        return JNI_ERR;
    }
    return JNI_OK;
}
