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
		return false;
	}

	ADDRESS buffer = read<ADDRESS>(stateManager + 0x4);
	if (!buffer) {
		return false;
	}

	ADDRESS objectCount = read<ADDRESS>(buffer + 0x4);
	if (!objectCount) {
		return false;
	}

	ADDRESS objectEntry = read<ADDRESS>(buffer + 0xC);
	if (!objectEntry) {
		return false;
	}

	ADDRESS lastObject = read<ADDRESS>(objectEntry + objectCount * 0x4 - 0x4);
	if (!lastObject) {
		return false;
	}

	return read<int>(lastObject + 0xC);
}

bool MemoryManager::MenuManager::isInGame() {
	return getMenuState() == 4; // menuState == 3 == in menu
}

bool MemoryManager::MenuManager::isInMenu() {
	return getMenuState() == 3;
}