// Created by Denys on 05.07.2023.

#pragma once

/* CLASS NAMES ===================================================================================*/

#define CLASS_NATIVE_BRIDGE_KOTLIN "com/iwex/poolpredictor/data/NativeBridge"

/*================================================================================================*/



/* METHOD NAMES ==================================================================================*/

#define METHOD_UPDATE_AIM_SETTINGS "updateAimSettings"

#define METHOD_EXIT_THREAD "exitThread"

#define METHOD_SET_ESP_VIEW "setEspView"

#define METHOD_GET_POCKET_POSITIONS_IN_SCREEN "getPocketPositionsInScreen"

#define METHOD_UPDATE_ESP_DATA "updateEspData"

/*================================================================================================*/



/* METHOD SIGNATURES =============================================================================*/

#define SIG_UPDATE_AIM_SETTINGS "(ZZZZII)V"

#define SIG_EXIT_THREAD "()V"

#define SIG_SET_ESP_VIEW "(Lcom/iwex/poolpredictor/presentation/view/EspView;)V"

#define SIG_GET_POCKET_POSITIONS_IN_SCREEN "(FFFF)[F"

#define SIG_UPDATE_ESP_DATA "([F)V"

/*================================================================================================*/
