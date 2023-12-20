#pragma once
#include <cmath>

namespace NumberUtils {

    // sub_1C2B0D8 5.8.0 angle, power, spin, ball positions??? should be truncated 
    inline double truncateTo4Places(double value) {
        return static_cast<int>(value * 10000.0) / 10000.0;
    }

    inline double calcAngle(const Point2D& delta) {
        double angle;

        if (delta.x == 0.0) {
            angle = PI_1_5;
            if (delta.y >= 0.0)
                angle = PI_0_5;
        }
        else {
            angle = atan(delta.y / delta.x);
            if (delta.x < 0.0)
                angle += PI;
        }

        return angle;
    }

    inline double calcAngle(const Point2D& source, const Point2D& Destination) {
        return calcAngle(source - Destination);
    }
}