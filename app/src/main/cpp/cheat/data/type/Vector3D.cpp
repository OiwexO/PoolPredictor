// Created by Denys on 05.07.2023.

#include "Vector3D.h"
#include <cmath>

Vector3D::Vector3D() : x(0.0f), y(0.0f), z(0.0f) {}

Vector3D::Vector3D(double x, double y, double z) : x(x), y(y), z(z) {}

void Vector3D::nullify() { x = y = z = 0.0; }

bool  Vector3D::isZero() const {
    return x == 0.0f && y == 0.0f && z == 0.0f;
}
