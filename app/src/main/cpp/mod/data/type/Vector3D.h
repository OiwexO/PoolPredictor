// Created by Denys on 05.07.2023.

#pragma once

class Vector3D {
public:
    double x;
    double y;
    double z;

    Vector3D() : x(0.0), y(0.0), z(0.0) {}

    Vector3D(double x, double y, double z) : x(x), y(y), z(z) {}

    inline void nullify() {
        x = y = z = 0.0;
    }

    inline bool isNotZero() const {
        return x != 0.0 || y != 0.0 || z != 0.0;
    };

};
