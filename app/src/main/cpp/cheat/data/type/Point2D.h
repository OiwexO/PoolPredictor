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

    Point2D();
    Point2D(double x, double y);

    inline double square() const {
        return (x * x + y * y);
    }

    double length() const;
    double distanceTo(const Point2D& destination) const;

    void nullify();
    bool isZero() const;
    ScreenPoint toScreen() const;

    Point2D operator-(const Point2D& other) const;
    Point2D operator*(const double value) const;
    bool operator==(const Point2D& other) const;
    bool operator!=(const Point2D& other) const;
    Point2D& operator=(const Point2D& other);

    static void setTableData(int tableLeft, int tableBottom, int tableRight);
};
