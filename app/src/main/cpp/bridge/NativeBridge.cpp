// Created by Denys on 04.07.2023.

#include <pthread.h>
#include <unistd.h>
#include "NativeBridge.h"
#include "NativeBridgeConstants.h"
#include "../cheat/data/GlobalSettings.h"
#include "../cheat/data/table/TableProperties.h"
#include "../cheat/predictor/Prediction.h"
#include "../utils/logger.h"
#include "../cheat/memory/MemoryManager.h"

const char* NativeBridge::TAG = "jni_NativeBridge";
bool NativeBridge::isShouldRunThread = true;
JavaVM* NativeBridge::mJvm = nullptr;
JNIEnv* NativeBridge::mEnv = nullptr;
jobject NativeBridge::mNativeRepository = nullptr;
jmethodID NativeBridge::mUpdatePredictionData = nullptr;
jfloatArray NativeBridge::mEmptyPredictionData = nullptr;

void* NativeBridge::cuePropertiesThread(void*) {
    while (NativeBridge::isShouldRunThread) {
        MemoryManager::CueProperties::writeCuePropertiesToMemory();
    }
    return nullptr;
}

void* NativeBridge::predictorThread(void*) {
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6;
    args.name = "predictorThread";
    args.group = nullptr;
    int attachResult = mJvm->AttachCurrentThread(&mEnv, &args);
    if(attachResult != JNI_OK) {
        LOGE(TAG, "Could not attach predictorThread");
        return nullptr;
    }
    NativeBridge::initEmptyPredictionData();
    bool isShouldRedraw;
    if (GlobalSettings::IS_DEBUG) {
        LOGD(TAG, "DEBUG build");
        while (NativeBridge::isShouldRunThread) {
            isShouldRedraw = gPrediction->mockPredictShotResult();
            if (isShouldRedraw) {
                NativeBridge::updatePredictionData(
                        gPrediction->getPredictionData(),
                        gPrediction->getPredictionDataSize()
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
                isShouldRedraw = gPrediction->predictShotResult();
                if (isShouldRedraw) {
                    NativeBridge::updatePredictionData(
                            gPrediction->getPredictionData(),
                            gPrediction->getPredictionDataSize()
                    );
                }
            } else {
                NativeBridge::clearPredictionData();
                sleep(1);
            }
        }
    }
    releaseGlobalRefs(mEnv);
    mJvm->DetachCurrentThread();
    LOGD(TAG, "Exiting predictorThread()");
    return nullptr;
}

void NativeBridge::initEmptyPredictionData() {
    mEmptyPredictionData = mEnv->NewFloatArray(2);
    jfloat initialValues[2] = {0.0f, 0.0f};
    mEnv->SetFloatArrayRegion(mEmptyPredictionData, 0, 2, initialValues);
    mEmptyPredictionData = (jfloatArray) mEnv->NewGlobalRef(mEmptyPredictionData);
}

// AimTabViewModel methods
void NativeBridge::updateAimSettings(
        JNIEnv*,
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

// PredictorService methods
void NativeBridge::setTablePosition(JNIEnv*, jclass, jfloat left, jfloat, jfloat right, jfloat bottom) {
//    LOGD(TAG, "left: %f top: %f right: %f bottom: %f", left, top, right, bottom);
    Point2D::setTableData(left, right, bottom);
    TableProperties::initializePocketPositionsInScreen();
}

void NativeBridge::setNativeRepository(JNIEnv* env, jclass, jobject nativeRepository) {
    env->GetJavaVM(&mJvm);
    mNativeRepository = env->NewGlobalRef(nativeRepository);
    if (setUpdatePredictionDataMethodId(env) != JNI_OK) {
        releaseGlobalRefs(env);
        return;
    }
    pthread_t thread;
    pthread_create(&thread, nullptr, predictorThread, nullptr);
}

int NativeBridge::setUpdatePredictionDataMethodId(JNIEnv* env) {
    jclass nativeRepositoryClass = env->GetObjectClass(mNativeRepository);
    if (!nativeRepositoryClass) {
        LOGE(TAG, "Could not find NativeRepository class");
        return JNI_ERR;
    }
    mUpdatePredictionData = env->GetMethodID(nativeRepositoryClass, METHOD_UPDATE_PREDICTION_DATA, SIG_UPDATE_PREDICTION_DATA);
    if (!mUpdatePredictionData) {
        LOGE(TAG, "Could not find updatePredictionData method in the NativeRepository class");
        return JNI_ERR;
    }
    return JNI_OK;
}

void NativeBridge::updatePredictionData(float* predictionData, int size) {
    jfloatArray jPredictionData = mEnv->NewFloatArray(size);
    mEnv->SetFloatArrayRegion(jPredictionData, 0, size, &(predictionData[0]));
    mEnv->CallVoidMethod(mNativeRepository, mUpdatePredictionData, jPredictionData);
    mEnv->DeleteLocalRef(jPredictionData);
}

void NativeBridge::clearPredictionData() {
    mEnv->CallVoidMethod(mNativeRepository, mUpdatePredictionData, mEmptyPredictionData);
}

void NativeBridge::releaseGlobalRefs(JNIEnv *env) {
    if (mNativeRepository != nullptr) {
        env->DeleteGlobalRef(mNativeRepository);
        mNativeRepository = nullptr;
    }
    if (mEmptyPredictionData != nullptr) {
        env->DeleteGlobalRef(mEmptyPredictionData);
        mEmptyPredictionData = nullptr;
    }
}

void NativeBridge::exitThread(JNIEnv*, jclass) {
    isShouldRunThread = false;
}

int NativeBridge::registerNativeMethods(JNIEnv* env) {
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
            {METHOD_UPDATE_AIM_SETTINGS,   SIG_UPDATE_AIM_SETTINGS,   reinterpret_cast<void*>(NativeBridge::updateAimSettings)},
            {METHOD_EXIT_THREAD,           SIG_EXIT_THREAD,           reinterpret_cast<void*>(NativeBridge::exitThread)},
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
