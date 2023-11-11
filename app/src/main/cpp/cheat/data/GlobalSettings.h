// Created by Denys on 14.06.2023.

#pragma once
#include <jni.h>

class GlobalSettings {
public:
    static bool IS_DEBUG;
    static bool isDrawLinesEnabled;
    static bool isDrawShotStateEnabled;
    static bool isDrawOpponentsLinesEnabled;
    static bool isPowerControlModeEnabled;
    static bool isCueStatsInitialized;
    static int cueAccuracy;
    static int cuePower;
    static int cueSpin;

};

