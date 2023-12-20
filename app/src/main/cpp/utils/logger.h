// Created by Denys on 13.06.2023.

#pragma once
#include <android/log.h>

#ifdef ENABLE_LOGS
#define LOGD(TAG, ...) ((void)__android_log_print(3, TAG, __VA_ARGS__))
#define LOGE(TAG, ...) ((void)__android_log_print(6, TAG, __VA_ARGS__))
#define LOGI(TAG, ...) ((void)__android_log_print(4,  TAG, __VA_ARGS__))
#define LOGW(TAG, ...) ((void)__android_log_print(5,  TAG, __VA_ARGS__))
#else
#define LOGD(TAG, ...) ((void)0)
#define LOGE(TAG, ...) ((void)0)
#define LOGI(TAG, ...) ((void)0)
#define LOGW(TAG, ...) ((void)0)
#endif
