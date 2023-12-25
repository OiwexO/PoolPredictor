// Created by Denys on 05.07.2023.

#include <array>
#include "TableProperties.h"

const std::array<Point2D, TABLE_POCKETS_COUNT>& TableProperties::getPockets() {
    static const std::array<Point2D, TABLE_POCKETS_COUNT> POCKET_POSITIONS = {
            Point2D(-130.8, -67.3),
            Point2D(0, -72),
            Point2D(130.8, -67.3),
            Point2D(130.8, 67.3),
            Point2D(0, 72),
            Point2D(-130.8, 67.3)
    };
    return POCKET_POSITIONS;
}

const std::array<Point2D, TABLE_SHAPE_SIZE>& TableProperties::getTableShape() {
    static const std::array<Point2D, TABLE_SHAPE_SIZE> TABLE_SHAPE = {
            Point2D(-127, 53.5),
            Point2D(-136.9, 64.1),
            Point2D(-138.2, 69.2),
            Point2D(-136.7, 73.2),
            Point2D(-132.7, 74.7),
            Point2D(-127.6, 73.4),
            Point2D(-117, 63.5),
            Point2D(-7.8, 63.5),
            Point2D(-6.1, 68.6),
            Point2D(-5.7, 72.7),
            Point2D(-3.7, 75.4),
            Point2D(0, 76.7),
            Point2D(3.7, 75.4),
            Point2D(5.7, 72.7),
            Point2D(6.1, 68.6),
            Point2D(7.8, 63.5),
            Point2D(117, 63.5),
            Point2D(127.6, 73.4),
            Point2D(132.7, 74.7),
            Point2D(136.7, 73.2),
            Point2D(138.2, 69.2),
            Point2D(136.9, 64.1),
            Point2D(127, 53.5),
            Point2D(127, -53.5),
            Point2D(136.9, -64.1),
            Point2D(138.2, -69.2),
            Point2D(136.7, -73.2),
            Point2D(132.7, -74.7),
            Point2D(127.6, -73.4),
            Point2D(117, -63.5),
            Point2D(7.8, -63.5),
            Point2D(6.1, -68.6),
            Point2D(5.7, -72.7),
            Point2D(3.7, -75.4),
            Point2D(0, -76.7),
            Point2D(-3.7, -75.4),
            Point2D(-5.7, -72.7),
            Point2D(-6.1, -68.6),
            Point2D(-7.8, -63.5),
            Point2D(-117, -63.5),
            Point2D(-127.6, -73.4),
            Point2D(-132.7, -74.7),
            Point2D(-136.7, -73.2),
            Point2D(-138.2, -69.2),
            Point2D(-136.9, -64.1),
            Point2D(-127, -53.5)
    };
    return TABLE_SHAPE;
}

float* TableProperties::getPocketPositionsInScreen() {
    float* pocketPositions = new float[TABLE_POCKETS_COUNT * 2];
    auto pockets = getPockets();
    for (int pocketIndex = 0; pocketIndex < TABLE_POCKETS_COUNT; ++pocketIndex) {
        ScreenPoint screenPosition = pockets[pocketIndex].toScreen();
        pocketPositions[pocketIndex] = screenPosition.x;
        pocketPositions[pocketIndex + TABLE_POCKETS_COUNT] = screenPosition.y;
    }
    return pocketPositions;
}

/*
const Point2D TableProperties::POCKET_POSITIONS[] = {
        {-130.8, -67.3},
        {0,      -72},
        {130.8,  -67.3},
        {130.8,  67.3},
        {0,      72},
        {-130.8, 67.3}
};

const Point2D TableProperties::TABLE_SHAPE[] = {
        Point2D(-127, 53.5),
        Point2D(-136.9, 64.1),
        Point2D(-138.2, 69.2),
        Point2D(-136.7, 73.2),
        Point2D(-132.7, 74.7),
        Point2D(-127.6, 73.4),
        Point2D(-117, 63.5),
        Point2D(-7.8, 63.5),
        Point2D(-6.1, 68.6),
        Point2D(-5.7, 72.7),
        Point2D(-3.7, 75.4),
        Point2D(0, 76.7),
        Point2D(3.7, 75.4),
        Point2D(5.7, 72.7),
        Point2D(6.1, 68.6),
        Point2D(7.8, 63.5),
        Point2D(117, 63.5),
        Point2D(127.6, 73.4),
        Point2D(132.7, 74.7),
        Point2D(136.7, 73.2),
        Point2D(138.2, 69.2),
        Point2D(136.9, 64.1),
        Point2D(127, 53.5),
        Point2D(127, -53.5),
        Point2D(136.9, -64.1),
        Point2D(138.2, -69.2),
        Point2D(136.7, -73.2),
        Point2D(132.7, -74.7),
        Point2D(127.6, -73.4),
        Point2D(117, -63.5),
        Point2D(7.8, -63.5),
        Point2D(6.1, -68.6),
        Point2D(5.7, -72.7),
        Point2D(3.7, -75.4),
        Point2D(0, -76.7),
        Point2D(-3.7, -75.4),
        Point2D(-5.7, -72.7),
        Point2D(-6.1, -68.6),
        Point2D(-7.8, -63.5),
        Point2D(-117, -63.5),
        Point2D(-127.6, -73.4),
        Point2D(-132.7, -74.7),
        Point2D(-136.7, -73.2),
        Point2D(-138.2, -69.2),
        Point2D(-136.9, -64.1),
        Point2D(-127, -53.5)
};
*/
