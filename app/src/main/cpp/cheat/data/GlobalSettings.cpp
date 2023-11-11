// Created by Denys on 04.07.2023.

#include "GlobalSettings.h"

#include "../../utils/logger.h"
#include "../../utils/stringObfuscator.h"

//const char* GlobalSettings::TAG = OBFUSCATE("jni_GlobalSettings");

#ifdef BUILD_TYPE_DEBUG
bool GlobalSettings::IS_DEBUG = true;
#else
bool GlobalSettings::IS_DEBUG = false;
#endif

bool GlobalSettings::isDrawLinesEnabled = true;
bool GlobalSettings::isDrawShotStateEnabled = true;
bool GlobalSettings::isDrawOpponentsLinesEnabled = false;
bool GlobalSettings::isPowerControlModeEnabled = false;
bool GlobalSettings::isCueStatsInitialized = false;
int GlobalSettings::cueAccuracy = 13;
int GlobalSettings::cuePower = 0;
int GlobalSettings::cueSpin = 0;






