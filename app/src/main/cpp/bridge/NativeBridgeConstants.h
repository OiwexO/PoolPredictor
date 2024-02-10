// Created by Denys on 05.07.2023.

#pragma once

/* CLASS NAMES ===================================================================================*/

#define CLASS_NATIVE_BRIDGE_KOTLIN "com/iwex/poolpredictor/domain/NativeBridge$Companion"
//#define CLASS_ESP_VIEW_KOTLIN "com/iwex/poolpredictor/presentation/view/EspView"

/*================================================================================================*/



/* METHOD NAMES ==================================================================================*/

// AimTabViewModel kotlin methods
#define METHOD_SET_DRAW_LINES "setDrawLines"
#define METHOD_SET_DRAW_SHOT_STATE "setDrawShotState"
#define METHOD_SET_DRAW_OPPONENTS_LINES "setDrawOpponentsLines"
#define METHOD_SET_PRECISE_TRAJECTORIES_ENABLED "setPreciseTrajectoriesEnabled"
#define METHOD_SET_CUE_POWER "setCuePower"
#define METHOD_SET_CUE_SPIN "setCueSpin"

// OtherTabViewModel kotlin methods
#define METHOD_EXIT_THREAD "exitThread"

// PredictorService kotlin methods
#define METHOD_SET_ESP_VIEW "setEspView"
#define METHOD_GET_POCKET_POSITIONS_IN_SCREEN "getPocketPositionsInScreen"

// EspView kotlin methods
#define METHOD_UPDATE_ESP_DATA "updateEspData"

/*================================================================================================*/



/* METHOD SIGNATURES =============================================================================*/

// AimTabViewModel kotlin signatures
#define SIG_SET_DRAW_LINES "(Z)V"
#define SIG_SET_DRAW_SHOT_STATE "(Z)V"
#define SIG_SET_DRAW_OPPONENTS_LINES "(Z)V"
#define SIG_SET_PRECISE_TRAJECTORIES_ENABLED "(Z)V"
#define SIG_SET_CUE_POWER "(I)V"
#define SIG_SET_CUE_SPIN "(I)V"

// OtherTabViewModel kotlin methods
#define SIG_EXIT_THREAD "()V"

// PredictorService kotlin signatures
#define SIG_SET_ESP_VIEW "(Lcom/iwex/poolpredictor/presentation/view/EspView;)V"
#define SIG_GET_POCKET_POSITIONS_IN_SCREEN "(IIII)[F"

// EspView kotlin methods
#define SIG_UPDATE_ESP_DATA "([F)V"

/*================================================================================================*/
