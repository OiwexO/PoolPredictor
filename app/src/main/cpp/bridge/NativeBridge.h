// Created by Denys on 04.07.2023.

#pragma once

#include <jni.h>
#include <vector>

class NativeBridge {
private:
    static const char *TAG;
    static bool isShouldRunThread;
    static JavaVM *mJvm;
    static JNIEnv *mEnv;
    static jobject mNativeRepository;
    static jmethodID mUpdateShotResult;
    static jfloatArray mEmptyShotResult;

    // changes cue power and spin according to GlobalSettings
    static void *cuePropertiesThread(void *);

    // runs prediction and updates PredictionView
    static void *predictorThread(void *);

    static void initEmptyShotResult();

    static void updateAimSettings(
            JNIEnv *,
            jclass,
            jboolean drawLinesEnabled,
            jboolean drawShotStateEnabled,
            jboolean drawOpponentsLinesEnabled,
            jboolean preciseTrajectoriesEnabled,
            jint cuePower,
            jint cueSpin
    );

    static void setTablePosition(
            JNIEnv *,
            jclass,
            jfloat left,
            jfloat,
            jfloat right,
            jfloat bottom
    );

    // creates a GlobalRef for NativeRepository instance, starts predictorThread()
    static void setNativeRepository(
            JNIEnv *env,
            jclass,
            jobject nativeRepository
    );

    static int setUpdateShotResultMethodId(JNIEnv *env);

    static void updateShotResult(float *shotResult, int size);

    static void clearShotResult();

    static void releaseGlobalRefs(JNIEnv *env);

    static void exitThread(JNIEnv *, jclass);

public:
    static int registerNativeMethods(JNIEnv *env);

};
