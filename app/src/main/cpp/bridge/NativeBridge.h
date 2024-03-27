// Created by Denys on 04.07.2023.

#pragma once
#include <jni.h>
#include <vector>

class NativeBridge {
private:
    static const char* TAG;
    static bool isShouldRunThread;
    static JavaVM* mJvm;
    static JNIEnv* mEnv;
    static jobject mNativeRepository;
    static jmethodID mUpdatePredictionDataData;
    static jfloatArray mEmptyPredictionData;

    // changes cue power and spin according to GlobalSettings
    static void* cuePropertiesThread(void*);
    // runs prediction and updates EspView
    static void* predictorThread(void*);

    static void initEmptyPredictionData();

    // AimTabViewModel methods
    static void updateAimSettings(
            JNIEnv*,
            jclass,
            jboolean drawLinesEnabled,
            jboolean drawShotStateEnabled,
            jboolean drawOpponentsLinesEnabled,
            jboolean preciseTrajectoriesEnabled,
            jint cuePower,
            jint cueSpin
            );

    // PredictorService methods
    static jfloatArray getPocketPositionsInScreen(
            JNIEnv* env,
            jclass,
            jfloat left,
            jfloat top,
            jfloat right,
            jfloat bottom
            );

    // creates a GlobalRef for NativeRepository instance, starts predictorThread()
    static void setNativeRepository(JNIEnv* env, jclass, jobject nativeRepository);

    // obtains fun updatePredictionData(predictionData: FloatArray) method ID
    static int setUpdatePredictionDataMethodId(JNIEnv* env);

    // updates trajectories and shot state
    static void updatePredictionData(float* predictionData, int size);

    // clears EspView
    static void clearPredictionData();

    // releases GlobalRef to mNativeRepository
    static void releaseGlobalRefs(JNIEnv* env);

    static void exitThread(JNIEnv*, jclass);

public:
    static int registerNativeMethods(JNIEnv* env);

};
