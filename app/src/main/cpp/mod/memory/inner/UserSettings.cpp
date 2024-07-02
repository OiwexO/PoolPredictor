// Created by Denys on 13.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"

ADDRESS MemoryManager::UserSettings::wideGuideline = 0;

void MemoryManager::UserSettings::initialize(ADDRESS _sharedUserSettings) {
    wideGuideline = _sharedUserSettings + Offsets::UserSettings::WideGuideline;
}

void MemoryManager::UserSettings::switchWideGuideline(bool isEnabled) {
    write(wideGuideline, (unsigned char) isEnabled);
}

