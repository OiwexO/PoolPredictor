// Created by Denys on 13.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"

ADDRESS MemoryManager::MenuManager::menuStateManager = 0;

void MemoryManager::MenuManager::initialize(ADDRESS _sharedMenuManager) {
	menuStateManager = _sharedMenuManager + Offsets::MenuManager::MenuStateManager;
}

int MemoryManager::MenuManager::getMenuState() {
	ADDRESS stateManager = read<ADDRESS>(menuStateManager);
	if (!stateManager) {
		return 0;
	}
	ADDRESS buffer = read<ADDRESS>(stateManager + 0x4);
	if (!buffer) {
		return 0;
	}
	ADDRESS objectCount = read<ADDRESS>(buffer + 0x4);
	if (!objectCount) {
		return 0;
	}
	ADDRESS objectEntry = read<ADDRESS>(buffer + 0xC);
	if (!objectEntry) {
		return 0;
	}
	ADDRESS lastObject = read<ADDRESS>(objectEntry + objectCount * 0x4 - 0x4);
	if (!lastObject) {
		return 0;
	}
	return read<int>(lastObject + 0xC);
}

bool MemoryManager::MenuManager::isInGame() {
	return getMenuState() == 4;
}

bool MemoryManager::MenuManager::isInMenu() {
	return getMenuState() == 3;
}