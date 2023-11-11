// Created by Denys on 05.07.2023.

#pragma once

class Vector3D {
public:
    double x;
    double y;
    double z;

    Vector3D();
    Vector3D(double x, double y, double z);

    void nullify();
    bool isZero() const;

};
