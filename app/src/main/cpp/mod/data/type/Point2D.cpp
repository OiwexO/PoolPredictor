// Created by Denys on 05.07.2023.

#include "Point2D.h"
#include "../GameConstants.h"

double Point2D::TABLE_LEFT = 0.0;
double Point2D::TABLE_BOTTOM = 0.0;
double Point2D::TABLE_SCALE = 1.0;

ScreenPoint Point2D::toScreen() const {
    double positionX = this->x + TABLE_HALF_WIDTH;
    double positionY = -(this->y + TABLE_HALF_HEIGHT);
    double scrX = TABLE_LEFT + positionX * TABLE_SCALE;
    double scrY = TABLE_BOTTOM + positionY * TABLE_SCALE;
    return ScreenPoint{(float) scrX, (float) scrY};
}

Point2D Point2D::operator-(const Point2D &other) const {
    return {this->x - other.x, this->y - other.y};
}

Point2D Point2D::operator*(const double value) const {
    return {this->x * value, this->y * value};
}

bool Point2D::operator==(const Point2D &other) const {
    return ((this->x == other.x) && (this->y == other.x));
}

bool Point2D::operator!=(const Point2D &other) const {
    return ((this->x != other.x) || (this->y != other.y));
}

Point2D &Point2D::operator=(const Point2D &other) {
    if (this == &other) return *this;
    x = other.x;
    y = other.y;
    return *this;
}

void Point2D::setTableData(float tableLeft, float tableRight, float tableBottom) {
    Point2D::TABLE_LEFT = double(tableLeft);
    Point2D::TABLE_BOTTOM = double(tableBottom);
    Point2D::TABLE_SCALE = double(tableRight - tableLeft) / TABLE_WIDTH;
}
