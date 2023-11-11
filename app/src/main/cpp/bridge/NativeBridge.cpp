// Created by Denys on 04.07.2023.

#include <pthread.h>
#include <unistd.h>
#include "NativeBridge.h"
#include "NativeBridgeConstants.h"
#include "../cheat/data/GlobalSettings.h"
#include "../cheat/data/table/TableProperties.h"
#include "../cheat/data/type/Point2D.h"

#include "../cheat/predictor/Prediction.h"

#include "../utils/logger.h"
#include "../utils/stringObfuscator.h"
#include "../cheat/memory/MemoryManager.h"

const char* NativeBridge::TAG = OBFUSCATE("jni_NativeBridge");
bool NativeBridge::isShouldRunThread = true;
bool NativeBridge::isToastShown = false;
JavaVM* NativeBridge::mJvm = nullptr;
JNIEnv* NativeBridge::mEnv = nullptr;
jobject NativeBridge::mEspView = nullptr;
jmethodID NativeBridge::mUpdateEspData = nullptr;

void* NativeBridge::predictor_thread(void*) {
//    LOGD(TAG, OBFUSCATE("predictor_thread() started"));

    if (!isToastShown) {
        LOGE(TAG, OBFUSCATE("Service Context is not set"));
        return nullptr;
    }

    int attachResult;
//#ifdef JNI_VERSION_1_2
    attachResult = mJvm->AttachCurrentThread(&mEnv, nullptr);
//#else
//    attachResult = mJvm->AttachCurrentThread(mJvm, &env, NULL);
//#endif
    if(attachResult != JNI_OK) {
        LOGE(TAG, OBFUSCATE("Could not attach predictor_thread"));
        // can't call releaseGlobalRefs() here because JNIEnv pointer is nullptr
        return nullptr;
    }
//    LOGD(TAG, OBFUSCATE("predictor_thread() attached"));

    if (setUpdateEspDataMethodId() != JNI_OK) {
        LOGE(TAG, OBFUSCATE("Could not get methodID"));
        releaseGlobalRefs(mEnv);
        return nullptr;
    }

    bool isShouldRedraw;

    if (GlobalSettings::IS_DEBUG) {
        LOGD(TAG, OBFUSCATE("DEBUG build"));

        while (NativeBridge::isShouldRunThread) {
            isShouldRedraw = gPrediction->mockPredictShotResult();
            if (isShouldRedraw) {
                NativeBridge::updateEspData(gPrediction->getEspData());
            }
        }
    } else {
        LOGD(TAG, OBFUSCATE("RELEASE build"));
        if (!MemoryManager::initialize()) {
            LOGD(TAG, OBFUSCATE("Could not initialize MemoryManager"));
            return nullptr;
        }
//        LOGD(TAG, OBFUSCATE("GAME MODULE ADDRESS: %lu"), MemoryManager::getGameModuleBase());
//        LOGD(TAG, OBFUSCATE("SHARED GAME MANAGER: %lu"), MemoryManager::getSharedGameManager());
//        LOGD(TAG, OBFUSCATE("SHARED MENU MANAGER: %lu"), MemoryManager::getSharedMenuManager());

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
    LOGD(TAG, OBFUSCATE("Exiting prediction loop"));
    releaseGlobalRefs(mEnv);
    LOGD(TAG, OBFUSCATE("Exiting predictor_thread()"));
    return nullptr;
}

// AimTabViewModel methods
void NativeBridge::setDrawLines(JNIEnv*, jobject, bool value) {
    GlobalSettings::isDrawLinesEnabled = value;
//    LOGD(TAG, OBFUSCATE("setDrawLines: %d"), value);
}

void NativeBridge::setDrawShotState(JNIEnv*, jobject, bool value) {
    GlobalSettings::isDrawShotStateEnabled = value;
//    LOGD(TAG, OBFUSCATE("setDrawShotState: %d"), value);
}

void NativeBridge::setDrawOpponentsLines(JNIEnv*, jobject, bool value) {
    GlobalSettings::isDrawOpponentsLinesEnabled = value;
//    LOGD(TAG, OBFUSCATE("setDrawOpponentsLines: %d"), value);
}

void NativeBridge::setPowerControlModeEnabled(JNIEnv*, jobject, bool value) {
    GlobalSettings::isPowerControlModeEnabled = value;
//    LOGD(TAG, OBFUSCATE("setPowerControlModeEnabled: %d"), value);
}

void NativeBridge::setCuePower(JNIEnv*, jobject, int power) {
    GlobalSettings::cuePower = power;
//    LOGD(TAG, OBFUSCATE("cuePower: %d"), power);
}

void NativeBridge::setCueSpin(JNIEnv*, jobject, int spin) {
    GlobalSettings::cueSpin = spin;
//    LOGD(TAG, OBFUSCATE("cueSpin: %d"), spin);
}

// FloatingMenuService methods
jfloatArray NativeBridge::getPocketPositionsInScreen(JNIEnv* env, jobject, jint left, jint top, jint right, jint bottom) {
//    LOGD(TAG, OBFUSCATE("left: %d top: %d right: %d bottom: %d"), left, top, right, bottom);
    Point2D::setTableData(left, bottom, right);

    float* pocketPositions = TableProperties::getPocketPositionsToScreen();
    jfloatArray jPocketPositions = env->NewFloatArray(12);
    env->SetFloatArrayRegion(jPocketPositions, 0, 12, pocketPositions);
    return jPocketPositions;
}

void NativeBridge::setServiceContext(JNIEnv* env, jobject, jobject serviceContext) {
    jclass toastClass = env->FindClass(OBFUSCATE("android/widget/Toast"));
    jmethodID makeTextMethod =env->GetStaticMethodID(toastClass, OBFUSCATE("makeText"), OBFUSCATE("(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;"));
    jstring jText = env->NewStringUTF(MY_TOAST_TEXT);
    jobject toastObject = env->CallStaticObjectMethod(toastClass, makeTextMethod, serviceContext, jText, 1);
    jmethodID showMethod = env->GetMethodID(toastClass, OBFUSCATE("show"), OBFUSCATE("()V"));
    env->CallVoidMethod(toastObject, showMethod);
    isToastShown = true;
}

void NativeBridge::setEspView(JNIEnv* env, jobject, jobject espView) {
    env->GetJavaVM(&mJvm);
    mEspView = env->NewGlobalRef(espView);
    pthread_t thread;
    pthread_create(&thread, nullptr, predictor_thread, nullptr);
}

int NativeBridge::setUpdateEspDataMethodId() {
    jclass espViewClass = mEnv->GetObjectClass(mEspView);
    if (!espViewClass) {
        LOGE(TAG, OBFUSCATE("Could not find desired View class"));
        return JNI_ERR;
    }

    mUpdateEspData = mEnv->GetMethodID(espViewClass, METHOD_UPDATE_ESP_DATA,SIG_UPDATE_ESP_DATA);
    if (!mUpdateEspData) {
        LOGE(TAG, OBFUSCATE("Could not find desired method in View class"));
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

jstring NativeBridge::getIcon(JNIEnv *env, jobject) {
    //Use https://compresspng.com/ to compress image
    //Use https://www.base64encode.org/ to encode image to base64
    return env->NewStringUTF(MY_FLOATING_ICON);
}

void NativeBridge::exitThread(JNIEnv*, jobject) {
    isShouldRunThread = false;
}

int NativeBridge::registerNativeMethods(JNIEnv* env) {
    if (env == nullptr) {
        LOGE(TAG, OBFUSCATE("JNIEnv pointer is nullptr"));
        return JNI_ERR;
    }

    jclass kotlinNativeBridgeClass = env->FindClass(CLASS_NATIVE_BRIDGE_KOTLIN);
    if (!kotlinNativeBridgeClass) {
        LOGE(TAG, "Could not find NativeBridge class in Kotlin");
        return JNI_ERR;
    }

    JNINativeMethod methods[] = {
            // AimTabViewModel native methods
            {METHOD_SET_DRAW_LINES,SIG_SET_DRAW_LINES,reinterpret_cast<void*>(NativeBridge::setDrawLines)},
            {METHOD_SET_DRAW_SHOT_STATE,SIG_SET_DRAW_SHOT_STATE,reinterpret_cast<void*>(NativeBridge::setDrawShotState)},
            {METHOD_SET_DRAW_OPPONENTS_LINES,SIG_SET_DRAW_OPPONENTS_LINES,reinterpret_cast<void*>(NativeBridge::setDrawOpponentsLines)},
            {METHOD_SET_POWER_CONTROL_MODE_ENABLED,SIG_SET_POWER_CONTROL_MODE_ENABLED,reinterpret_cast<void*>(NativeBridge::setPowerControlModeEnabled)},
            {METHOD_SET_CUE_POWER,SIG_SET_CUE_POWER,reinterpret_cast<void*>(NativeBridge::setCuePower)},
            {METHOD_SET_CUE_SPIN,SIG_SET_CUE_SPIN,reinterpret_cast<void*>(NativeBridge::setCueSpin)},

            // OtherTabViewModel native methods
            {METHOD_EXIT_THREAD,SIG_EXIT_THREAD,reinterpret_cast<void*>(NativeBridge::exitThread)},

            // FloatingMenuLayout native methods
            {METHOD_GET_ICON,SIG_GET_ICON,reinterpret_cast<void*>(NativeBridge::getIcon)},

            // FloatingMenuService native methods
            {METHOD_SET_SERVICE_CONTEXT,SIG_SET_SERVICE_CONTEXT,reinterpret_cast<void*>(NativeBridge::setServiceContext)},
            {METHOD_SET_ESP_VIEW,SIG_SET_ESP_VIEW,reinterpret_cast<void*>(NativeBridge::setEspView)},
            {METHOD_GET_POCKET_POSITIONS_IN_SCREEN,SIG_GET_POCKET_POSITIONS_IN_SCREEN, reinterpret_cast<void*>(NativeBridge::getPocketPositionsInScreen)},

            //

    };

    if (env->RegisterNatives(kotlinNativeBridgeClass, methods, (sizeof(methods) / sizeof(methods[0]))) != JNI_OK) {
        LOGE(TAG, "Could not register native methods");
        return JNI_ERR;
    }
    return JNI_OK;
}
