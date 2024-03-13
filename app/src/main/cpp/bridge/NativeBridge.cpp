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
jobject NativeBridge::mEspView = nullptr;
jmethodID NativeBridge::mUpdateEspData = nullptr;
jfloatArray NativeBridge::mEmptyEspData = nullptr;

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
    NativeBridge::initEmptyEspData();
    bool isShouldRedraw;
    if (GlobalSettings::IS_DEBUG) {
        LOGD(TAG, "DEBUG build");
        while (NativeBridge::isShouldRunThread) {
            isShouldRedraw = gPrediction->mockPredictShotResult();
            if (isShouldRedraw) {
                NativeBridge::updateEspData(gPrediction->getEspData(), gPrediction->getEspDataSize());
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
            if ((GlobalSettings::isDrawLinesEnabled || GlobalSettings::isDrawShotStateEnabled) && MemoryManager::MenuManager::isInGame()) {
                if (MemoryManager::GameManager::isValidGameState(GlobalSettings::isDrawOpponentsLinesEnabled)) {
                    isShouldRedraw = gPrediction->predictShotResult();
                    if (isShouldRedraw) {
                        NativeBridge::updateEspData(gPrediction->getEspData(), gPrediction->getEspDataSize());
                    }
                }
            } else {
                NativeBridge::clearEspData();
                sleep(1);
            }
        }
    }
    releaseGlobalRefs(mEnv);
    mJvm->DetachCurrentThread();
    LOGD(TAG, "Exiting predictorThread()");
    return nullptr;
}

void NativeBridge::initEmptyEspData() {
    mEmptyEspData = mEnv->NewFloatArray(2);
    jfloat initialValues[2] = {0.0f, 0.0f};
    mEnv->SetFloatArrayRegion(mEmptyEspData, 0, 2, initialValues);
    mEmptyEspData = (jfloatArray) mEnv->NewGlobalRef(mEmptyEspData);
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
jfloatArray NativeBridge::getPocketPositionsInScreen(JNIEnv* env, jclass, jfloat left, jfloat, jfloat right, jfloat bottom) {
//    LOGD(TAG, "left: %f top: %f right: %f bottom: %f", left, top, right, bottom);
    Point2D::setTableData(left, right, bottom);
    float* pocketPositions = TableProperties::getPocketPositionsInScreen();
    jfloatArray jPocketPositions = env->NewFloatArray(TABLE_POCKETS_COUNT * 2);
    env->SetFloatArrayRegion(jPocketPositions, 0, TABLE_POCKETS_COUNT * 2, pocketPositions);
    delete[] pocketPositions;
    return jPocketPositions;
}

void NativeBridge::setEspView(JNIEnv* env, jclass, jobject espView) {
    env->GetJavaVM(&mJvm);
    mEspView = env->NewGlobalRef(espView);
    if (setUpdateEspDataMethodId(env) != JNI_OK) {
        LOGE(TAG, "Could not get methodID");
        releaseGlobalRefs(env);
        return;
    }
    pthread_t thread;
    pthread_create(&thread, nullptr, predictorThread, nullptr);
}

int NativeBridge::setUpdateEspDataMethodId(JNIEnv* env) {
    jclass espViewClass = env->GetObjectClass(mEspView);
    if (!espViewClass) {
        LOGE(TAG, "Could not find desired View class");
        return JNI_ERR;
    }
    mUpdateEspData = env->GetMethodID(espViewClass, METHOD_UPDATE_ESP_DATA,SIG_UPDATE_ESP_DATA);
    if (!mUpdateEspData) {
        LOGE(TAG, "Could not find desired method in View class");
        return JNI_ERR;
    }
    return JNI_OK;
}

void NativeBridge::updateEspData(float* espData, int size) {
    jfloatArray jEspData = mEnv->NewFloatArray(size);
    mEnv->SetFloatArrayRegion(jEspData, 0, size, &(espData[0]));
    delete[] espData;
    mEnv->CallVoidMethod(mEspView, mUpdateEspData, jEspData);
    mEnv->DeleteLocalRef(jEspData);
}

void NativeBridge::clearEspData() {
    mEnv->CallVoidMethod(mEspView, mUpdateEspData, mEmptyEspData);
}

void NativeBridge::releaseGlobalRefs(JNIEnv *env) {
    if (mEspView != nullptr) {
        env->DeleteGlobalRef(mEspView);
        mEspView = nullptr;
    }
    if (mEmptyEspData != nullptr) {
        env->DeleteGlobalRef(mEmptyEspData);
        mEmptyEspData = nullptr;
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
            // AimTabViewModel native methods
            {METHOD_UPDATE_AIM_SETTINGS,              SIG_UPDATE_AIM_SETTINGS,              reinterpret_cast<void*>(NativeBridge::updateAimSettings)},

            // OtherTabViewModel native methods
            {METHOD_EXIT_THREAD,                      SIG_EXIT_THREAD,                      reinterpret_cast<void*>(NativeBridge::exitThread)},

            // PredictorService native methods
            {METHOD_SET_ESP_VIEW,                     SIG_SET_ESP_VIEW,                     reinterpret_cast<void*>(NativeBridge::setEspView)},
            {METHOD_GET_POCKET_POSITIONS_IN_SCREEN,   SIG_GET_POCKET_POSITIONS_IN_SCREEN,   reinterpret_cast<void*>(NativeBridge::getPocketPositionsInScreen)},

            //

    };
    int nMethods = (sizeof(methods) / sizeof(methods[0]));
    int registrationResult = env->RegisterNatives(kotlinNativeBridgeClass, methods, nMethods);
    if (registrationResult != JNI_OK) {
        LOGE(TAG, "Could not register native methods");
        return JNI_ERR;
    }
    return JNI_OK;
}
