// Created by Denys on 05.07.2023.

#pragma once

// ENUMS FOR GAME DATA

enum BallState : int {
    DEFAULT = 1,
    IN_POCKET = 2,
    UNKNOWN = 3,
    POTTED = 4,
    ERR_STATE = -8
};

enum BallClassification : int {
    ANY = -1, // 0xFFFFFFFF
    CUE_BALL = 0,
    SOLID = 1,
    STRIPE = 2,
    NINE_BALL_RULE = 3,
    EIGHT_BALL = 4,
    ERR_CLASSIFICATION = -8
};



// BALL PROPERTIES

constexpr int MAX_BALLS_COUNT = 16;

constexpr double BALL_RADIUS = 3.800475;

constexpr double BALL_RADIUS_SQUARE = BALL_RADIUS * BALL_RADIUS;



// TABLE SIZE CONSTANTS

constexpr double TABLE_WIDTH = 254.0;
constexpr double TABLE_HEIGHT = 127.0;

constexpr double TABLE_HALF_WIDTH = TABLE_WIDTH / 2.0;
constexpr double TABLE_HALF_HEIGHT = TABLE_HEIGHT / 2.0;

constexpr double TABLE_BOUND_LEFT = -TABLE_HALF_WIDTH + BALL_RADIUS;
constexpr double TABLE_BOUND_TOP = -TABLE_HALF_HEIGHT + BALL_RADIUS;
constexpr double TABLE_BOUND_RIGHT = TABLE_HALF_WIDTH - BALL_RADIUS;
constexpr double TABLE_BOUND_BOTTOM = TABLE_HALF_HEIGHT - BALL_RADIUS;



// OTHER TABLE PROPERTIES

constexpr int TABLE_POCKETS_COUNT = 6;
constexpr int TABLE_SHAPE_SIZE = 46;

constexpr double POCKET_RADIUS = 8.0;

constexpr double POCKET_RADIUS_SQUARE = POCKET_RADIUS * POCKET_RADIUS;

constexpr int MAX_SHOT_RESULT_SIZE = 50000;



// OTHER GAME PROPERTIES

constexpr double TIME_PER_TICK = 0.005;
constexpr double MIN_TIME = 1E-11;



// ANGLE CONSTANTS

constexpr double PI = 3.14159265358979;
constexpr double PI_0_5 = PI / 2.0;
constexpr double PI_1_5 = PI * 1.5;

constexpr double MAX_ANGLE_RADIANS = 360.0 / (180.0 / PI);

constexpr double MIN_ANGLE_STEP_RADIANS = 0.0174;