// Created by Denys on 13.06.2023.

#pragma once
#include <android/log.h>

#define LOGD(TAG, ...) ((void)__android_log_print(3, TAG, __VA_ARGS__))
#define LOGE(TAG, ...) ((void)__android_log_print(6, TAG, __VA_ARGS__))
#define LOGI(TAG, ...) ((void)__android_log_print(4,  TAG, __VA_ARGS__))
#define LOGW(TAG, ...) ((void)__android_log_print(5,  TAG, __VA_ARGS__))

/*
#define DEFAULT_TAG OBFUSCATE("iwex")
#define LOGD(...) ((void)__android_log_print(3, DEFAULT_TAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(6, DEFAULT_TAG, __VA_ARGS__))
#define LOGI(...) ((void)__android_log_print(4,  DEFAULT_TAG, __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(5,  DEFAULT_TAG, __VA_ARGS__))
*/
