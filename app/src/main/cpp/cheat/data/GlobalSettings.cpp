// Created by Denys on 04.07.2023.

#include "GlobalSettings.h"

#ifdef BUILD_TYPE_DEBUG
bool GlobalSettings::IS_DEBUG = true;
#else
bool GlobalSettings::IS_DEBUG = false;
#endif

bool GlobalSettings::isDrawLinesEnabled = false;
bool GlobalSettings::isDrawShotStateEnabled = false;
bool GlobalSettings::isDrawOpponentsLinesEnabled = false;
bool GlobalSettings::isPreciseTrajectoriesEnabled = false;
bool GlobalSettings::isCueStatsInitialized = false;
int GlobalSettings::cueAccuracy = 13;
int GlobalSettings::cuePower = 0;
int GlobalSettings::cueSpin = 0;






