// Created by Denys on 05.07.2023.

#pragma once

#include "../type/Point2D.h"
#include "../GameConstants.h"

class TableProperties {
private:
    static ScreenPoint pocketPositionsInScreen[TABLE_POCKETS_COUNT];

public:
    static const std::array<Point2D, TABLE_POCKETS_COUNT> &getPockets();

    static const std::array<Point2D, TABLE_SHAPE_SIZE> &getTableShape();

    static void initializePocketPositionsInScreen();

    static ScreenPoint *getPocketPositionsInScreen();

//    static const Point2D POCKET_POSITIONS[TABLE_POCKETS_COUNT];
//    static const Point2D TABLE_SHAPE[TABLE_SHAPE_SIZE];
};
