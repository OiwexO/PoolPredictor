// Created by Denys on 05.07.2023.

#pragma once
#include "../utils/stringObfuscator.h"

/* CLASS NAMES ===================================================================================*/

#define CLASS_NATIVE_BRIDGE_KOTLIN OBFUSCATE("com/iwex/poolpredictor/app/NativeBridge$Companion")
//#define CLASS_ESP_VIEW_KOTLIN OBFUSCATE("com/iwex/poolpredictor/app/view/EspView")

/*================================================================================================*/



/* METHOD NAMES ==================================================================================*/

// AimTabViewModel kotlin methods
#define METHOD_SET_DRAW_LINES OBFUSCATE("setDrawLines")
#define METHOD_SET_DRAW_SHOT_STATE OBFUSCATE("setDrawShotState")
#define METHOD_SET_DRAW_OPPONENTS_LINES OBFUSCATE("setDrawOpponentsLines")
#define METHOD_SET_POWER_CONTROL_MODE_ENABLED OBFUSCATE("setPowerControlModeEnabled")
#define METHOD_SET_CUE_POWER OBFUSCATE("setCuePower")
#define METHOD_SET_CUE_SPIN OBFUSCATE("setCueSpin")

// OtherTabViewModel kotlin methods
#define METHOD_EXIT_THREAD OBFUSCATE("exitThread")

// FloatingMenuService kotlin methods
#define METHOD_SET_ESP_VIEW OBFUSCATE("setEspView")
#define METHOD_GET_POCKET_POSITIONS_IN_SCREEN OBFUSCATE("getPocketPositionsInScreen")

// EspView kotlin methods
#define METHOD_UPDATE_ESP_DATA OBFUSCATE("updateEspData")

/*================================================================================================*/



/* METHOD SIGNATURES =============================================================================*/

// AimTabViewModel kotlin signatures
#define SIG_SET_DRAW_LINES OBFUSCATE("(Z)V")
#define SIG_SET_DRAW_SHOT_STATE OBFUSCATE("(Z)V")
#define SIG_SET_DRAW_OPPONENTS_LINES OBFUSCATE("(Z)V")
#define SIG_SET_POWER_CONTROL_MODE_ENABLED OBFUSCATE("(Z)V")
#define SIG_SET_CUE_POWER OBFUSCATE("(I)V")
#define SIG_SET_CUE_SPIN OBFUSCATE("(I)V")

// OtherTabViewModel kotlin methods
#define SIG_EXIT_THREAD OBFUSCATE("()V")

// FloatingMenuService kotlin signatures
#define SIG_SET_ESP_VIEW OBFUSCATE("(Lcom/iwex/poolpredictor/app/view/EspView;)V")
#define SIG_GET_POCKET_POSITIONS_IN_SCREEN OBFUSCATE("(IIII)[F")

// EspView kotlin methods
#define SIG_UPDATE_ESP_DATA OBFUSCATE("([F)V")

/*================================================================================================*/
