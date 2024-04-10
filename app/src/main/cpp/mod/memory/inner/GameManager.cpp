// Created by Denys on 13.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"

ADDRESS MemoryManager::GameManager::gameStateManager = 0;
ADDRESS MemoryManager::GameManager::gameRules = 0;
ADDRESS MemoryManager::GameManager::gameMode = 0;
ADDRESS MemoryManager::GameManager::table = 0;

void MemoryManager::GameManager::initialize(ADDRESS _sharedGameManager) {
	gameStateManager = _sharedGameManager + Offsets::GameManager::StateManager;
	gameRules = _sharedGameManager + Offsets::GameManager::Rules;
	gameMode = _sharedGameManager + Offsets::GameManager::GameMode;
	table = _sharedGameManager + Offsets::GameManager::Table;
}

ADDRESS MemoryManager::GameManager::getGameRules() {
	return read<ADDRESS>(gameRules);
}

int MemoryManager::GameManager::getGameState() {
	ADDRESS stateManager = read<ADDRESS>(gameStateManager);
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
	int gameState = read<int>(lastObject + 0xC);
	return gameState;
}

bool MemoryManager::GameManager::isValidGameState(bool isDrawOpponentsLinesEnabled) {
	int state = getGameState();
	if (isDrawOpponentsLinesEnabled) {
		return (state == 4 || state == 7);
	} else {
		return (state == 4);
	}
}

int MemoryManager::GameManager::getGameMode() {
	return read<int>(gameMode);
}

ADDRESS MemoryManager::GameManager::getTable() {
	return read<ADDRESS>(table);
}

bool MemoryManager::GameManager::is9BallMode() {
	ADDRESS rules = getGameRules();
	int gameModeRule = read<int>(rules);
	return (gameModeRule & 64) == 64;
}

bool MemoryManager::GameManager::isPlayerTurn() {
	return (getGameState() == 4);
}

BallClassification MemoryManager::GameManager::getPlayerClassification(bool isLocalPlayer) {
	ADDRESS rules = getGameRules();
	auto playerClassification = read<ADDRESS>(rules + Offsets::GameManager::PlayerClassification);
	ADDRESS player = (isLocalPlayer) ? 0x0 : 0x4;
	int classification = read<int>(playerClassification + player);
	switch (classification) {
	case BallClassification::ANY:
		return BallClassification::ANY;
	case BallClassification::SOLID:
		return BallClassification::SOLID;
	case BallClassification::STRIPE:
		return BallClassification::STRIPE;
	case BallClassification::NINE_BALL_RULE:
		return BallClassification::NINE_BALL_RULE;
	case BallClassification::EIGHT_BALL:
		return BallClassification::EIGHT_BALL;
	default:
		return BallClassification::ERR_CLASSIFICATION;
	}
}
