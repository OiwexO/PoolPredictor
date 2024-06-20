// Created by Denys on 12.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"

int MemoryManager::Balls::ballsCount = 0;
ADDRESS MemoryManager::Balls::ballsList = 0;

ADDRESS MemoryManager::Balls::getBallByIndex(int ballIndex) {
    return read<ADDRESS>(ballsList + ballIndex * 4);
}

void MemoryManager::Balls::initializeBallsList() {
    ADDRESS table = MemoryManager::GameManager::getTable();
    ADDRESS balls = read<ADDRESS>(table + Offsets::Balls::Balls);
    ballsCount = read<int>(balls + Offsets::Balls::Count);
    ballsList = read<ADDRESS>(balls + Offsets::Balls::Entry);
}

Point2D MemoryManager::Balls::getBallPosition(int ballIndex) {
    ADDRESS ball = getBallByIndex(ballIndex);
    return read<Point2D>(ball + Offsets::Ball::Position);
}

BallClassification MemoryManager::Balls::getBallClassification(int ballIndex) {
    ADDRESS ball = getBallByIndex(ballIndex);
    int classification = read<int>(ball + Offsets::Ball::Classification);
    switch (classification) {
        case BallClassification::ANY:
        case BallClassification::SOLID:
        case BallClassification::STRIPE:
        case BallClassification::NINE_BALL_RULE:
        case BallClassification::EIGHT_BALL:
            return static_cast<BallClassification>(classification);
        default:
            return BallClassification::ERR_CLASSIFICATION;
    }
}

BallState MemoryManager::Balls::getBallState(int ballIndex) {
    ADDRESS ball = getBallByIndex(ballIndex);
    int state = read<int>(ball + Offsets::Ball::State);
    switch (state) {
        case BallState::DEFAULT:
        case BallState::IN_POCKET:
        case BallState::UNKNOWN:
        case BallState::POTTED:
            return static_cast<BallState>(state);
        default:
            return BallState::ERR_STATE;
    }
}

bool MemoryManager::Balls::isBallOnTable(BallState ballState) {
    return (ballState == BallState::DEFAULT) || (ballState == BallState::IN_POCKET);
}
