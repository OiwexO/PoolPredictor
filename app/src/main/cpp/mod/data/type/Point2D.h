// Created by Denys on 05.07.2023.
#pragma once

#include "ScreenPoint.h"

class Point2D {
private:
    // used to scale game points to screen points
    static double TABLE_LEFT;
    static double TABLE_BOTTOM;
    static double TABLE_SCALE;

public:
    double x;
    double y;

    Point2D() : x(0.0), y(0.0) {}

    Point2D(double x, double y) : x(x), y(y) {}

    inline double square() const {
        return (x * x + y * y);
    }

    inline void nullify() {
        x = y = 0.0;
    }

    inline bool isZero() const {
        return x == 0.0 && y == 0.0;
    }

    inline bool isNotZero() const {
        return x != 0.0 || y != 0.0;
    };

    ScreenPoint toScreen() const;

    Point2D operator-(const Point2D &other) const;

    Point2D operator*(double value) const;

    bool operator==(const Point2D &other) const;

    bool operator!=(const Point2D &other) const;

    Point2D &operator=(const Point2D &other);

    static void setTableData(float tableLeft, float tableRight, float tableBottom);
};
