// Created by Denys on 13.07.2023.

#include "MemoryManager.h"
#include "Offsets.h"

#include <unistd.h>
#include <cstring>
#include <cstdio>
#include <cstdlib>

ADDRESS MemoryManager::gameModuleBase = 0;
ADDRESS MemoryManager::sharedGameManager = 0;
ADDRESS MemoryManager::sharedMenuManager = 0;
ADDRESS MemoryManager::sharedUserSettings = 0;

ADDRESS MemoryManager::findModuleBase(const char *moduleName) {
    FILE *maps = fopen("/proc/self/maps", "rt");
    if (maps == nullptr) return 0;
    char line[512] = {0};
    ADDRESS address = 0;
    while (fgets(line, sizeof(line), maps)) {
        if (strstr(line, moduleName)) {
            address = (ADDRESS) strtoul(line, nullptr, 16);
            break;
        }
    }
    fclose(maps);
    return address;
}

bool MemoryManager::initialize() {
    while (gameModuleBase <= 0) {
        gameModuleBase = findModuleBase("libgame-BPM-GooglePlay-Gold-Release-Module");
        sleep(1);
    }
    bool isShouldUpdate = true;
    while (isShouldUpdate) {
        sharedGameManager = MemoryManager::read<ADDRESS>(
                gameModuleBase + Offsets::SharedManager::SharedGameManager);
        sharedMenuManager = MemoryManager::read<ADDRESS>(
                gameModuleBase + Offsets::SharedManager::SharedMenuManager);
        sharedUserSettings = MemoryManager::read<ADDRESS>(
                gameModuleBase + Offsets::SharedManager::SharedUserSettings);
        isShouldUpdate = ((!sharedGameManager) || (!sharedMenuManager) || (!sharedUserSettings));
        sleep(1);
    }
    GameManager::initialize(sharedGameManager);
    VisualCue::initialize(sharedGameManager);
    MenuManager::initialize(sharedMenuManager);
    UserSettings::initialize(sharedUserSettings);
    CueProperties::initialize(gameModuleBase);
    return true;
}
