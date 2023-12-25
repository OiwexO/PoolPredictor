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

void* NativeBridge::cuePropertiesThread(void*) {
    while (NativeBridge::isShouldRunThread) {
        MemoryManager::CueProperties::setCuePower(GlobalSettings::cuePower);
        MemoryManager::CueProperties::setCueSpin(GlobalSettings::cueSpin);
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
    bool isShouldRedraw;
    if (GlobalSettings::IS_DEBUG) {
        LOGD(TAG, "DEBUG build");
        while (NativeBridge::isShouldRunThread) {
            isShouldRedraw = gPrediction->mockPredictShotResult();
            if (isShouldRedraw) {
                NativeBridge::updateEspData(gPrediction->getEspData());
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
            if (MemoryManager::MenuManager::isInGame()) {
                if (MemoryManager::GameManager::isValidGameState(GlobalSettings::isDrawOpponentsLinesEnabled)) {
                    isShouldRedraw = gPrediction->predictShotResult();
                    if (isShouldRedraw) {
                        NativeBridge::updateEspData(gPrediction->getEspData());
                    }
                }
            } else {
                NativeBridge::updateEspData(std::vector<float>(2));
                sleep(1);
            }
        }
    }
    releaseGlobalRefs(mEnv);
    mJvm->DetachCurrentThread();
    LOGD(TAG, "Exiting predictorThread()");
    return nullptr;
}

// AimTabViewModel methods
void NativeBridge::setDrawLines(JNIEnv*, jobject, jboolean value) {
    GlobalSettings::isDrawLinesEnabled = value;
}

void NativeBridge::setDrawShotState(JNIEnv*, jobject, jboolean value) {
    GlobalSettings::isDrawShotStateEnabled = value;
}

void NativeBridge::setDrawOpponentsLines(JNIEnv*, jobject, jboolean value) {
    GlobalSettings::isDrawOpponentsLinesEnabled = value;
}

void NativeBridge::setPreciseTrajectoriesEnabled(JNIEnv*, jobject, jboolean value) {
    GlobalSettings::isPreciseTrajectoriesEnabled = value;
}

void NativeBridge::setCuePower(JNIEnv*, jobject, jint power) {
    GlobalSettings::cuePower = power;
}

void NativeBridge::setCueSpin(JNIEnv*, jobject, jint spin) {
    GlobalSettings::cueSpin = spin;
}

// PredictorService methods
jfloatArray NativeBridge::getPocketPositionsInScreen(JNIEnv* env, jobject, jint left, jint, jint right, jint bottom) {
//    LOGD(TAG, "left: %d top: %d right: %d bottom: %d", left, top, right, bottom);
    Point2D::setTableData(left, right, bottom);
    float* pocketPositions = TableProperties::getPocketPositionsInScreen();
    jfloatArray jPocketPositions = env->NewFloatArray(TABLE_POCKETS_COUNT * 2);
    env->SetFloatArrayRegion(jPocketPositions, 0, TABLE_POCKETS_COUNT * 2, pocketPositions);
    delete[] pocketPositions;
    return jPocketPositions;
}

void NativeBridge::setEspView(JNIEnv* env, jobject, jobject espView) {
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

void NativeBridge::updateEspData(const std::vector<float>& espData) {
    jfloatArray jEspData = mEnv->NewFloatArray((int) espData.size());
    mEnv->SetFloatArrayRegion(jEspData, 0, (int) espData.size(), &(espData[0]));
    mEnv->CallVoidMethod(mEspView, mUpdateEspData, jEspData);
    mEnv->DeleteLocalRef(jEspData);
}

void NativeBridge::releaseGlobalRefs(JNIEnv *env) {
    if (mEspView != nullptr) {
        env->DeleteGlobalRef(mEspView);
        mEspView = nullptr;
    }
}

void NativeBridge::exitThread(JNIEnv*, jobject) {
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
            {METHOD_SET_DRAW_LINES,                   SIG_SET_DRAW_LINES,                   reinterpret_cast<void*>(NativeBridge::setDrawLines)},
            {METHOD_SET_DRAW_SHOT_STATE,              SIG_SET_DRAW_SHOT_STATE,              reinterpret_cast<void*>(NativeBridge::setDrawShotState)},
            {METHOD_SET_DRAW_OPPONENTS_LINES,         SIG_SET_DRAW_OPPONENTS_LINES,         reinterpret_cast<void*>(NativeBridge::setDrawOpponentsLines)},
            {METHOD_SET_PRECISE_TRAJECTORIES_ENABLED, SIG_SET_PRECISE_TRAJECTORIES_ENABLED, reinterpret_cast<void *>(NativeBridge::setPreciseTrajectoriesEnabled)},
            {METHOD_SET_CUE_POWER,                    SIG_SET_CUE_POWER,                    reinterpret_cast<void*>(NativeBridge::setCuePower)},
            {METHOD_SET_CUE_SPIN,                     SIG_SET_CUE_SPIN,                     reinterpret_cast<void*>(NativeBridge::setCueSpin)},

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
