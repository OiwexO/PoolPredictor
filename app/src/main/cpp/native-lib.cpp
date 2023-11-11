// Created by Denys on 13.06.2023.

#include <jni.h>
#include "bridge/NativeBridge.h"

extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *) {
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (NativeBridge::registerNativeMethods(env) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}
