// Created by Denys on 05.07.2023.
#pragma once
#include "ScreenPoint.h"

class Point2D {
private:
    // used to convert game points to screen points
    static double TABLE_LEFT;
    static double TABLE_BOTTOM;
    static double TABLE_SCALE;

public:
    double x;
    double y;

    Point2D() : x(0.0f), y(0.0f) {}

    Point2D(double x, double y) : x(x), y(y) {}

    inline double square() const {
        return (x * x + y * y);
    }

    inline void nullify() {
        x = y = 0.0;
    }

    inline bool isZero() const {
        return x == 0.0f && y == 0.0f;
    }

    inline bool isNotZero() const {
        return x != 0.0f || y != 0.0f;
    };

    ScreenPoint toScreen() const;

    Point2D operator-(const Point2D& other) const;
    Point2D operator*(const double value) const;
    bool operator==(const Point2D& other) const;
    bool operator!=(const Point2D& other) const;
    Point2D& operator=(const Point2D& other);

    static void setTableData(int tableLeft, int tableBottom, int tableRight);
};
