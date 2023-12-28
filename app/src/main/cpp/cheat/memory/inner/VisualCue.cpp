// Created by Denys on 13.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"
#include "../../../utils/NumberUtils.h"

ADDRESS MemoryManager::VisualCue::visualCue = 0;
ADDRESS MemoryManager::VisualCue::spinObject = 0;

double MemoryManager::VisualCue::DEFAULT_POWER = 1.0;

void MemoryManager::VisualCue::initialize(ADDRESS _sharedGameManager) {
	visualCue = _sharedGameManager + Offsets::VisualCue::VisualCue;
	spinObject = _sharedGameManager + Offsets::VisualCue::SpinObject;
}

double MemoryManager::VisualCue::getShotAngle() {
	ADDRESS _visualCue = read<ADDRESS>(visualCue);
	ADDRESS visualGuide = read<ADDRESS>(_visualCue + Offsets::VisualCue::VisualGuide);
    double angle = read<double>(visualGuide + Offsets::VisualCue::AimAngle);
	if (angle < 0.0 || angle > MAX_ANGLE_RADIANS) {
		return DEFAULT_ANGLE;
	}
	return NumberUtils::truncateTo4Places(angle);
}

double MemoryManager::VisualCue::getShotPower() {
	ADDRESS _visualCue = read<ADDRESS>(visualCue);
	double power = read<double>(_visualCue + Offsets::VisualCue::Power);
	if (power <= 0.0 || power > 1.0) {
		power = DEFAULT_POWER;
	}
	else {
		power = NumberUtils::truncateTo4Places(power);
	}
	return (1.0 - sqrt(1.0 - power)) * CueProperties::getCuePower();
}

void MemoryManager::VisualCue::setShotPower(const double power) {
	ADDRESS _visualCue = read<ADDRESS>(visualCue);
	write(_visualCue + Offsets::VisualCue::Power, power);
}

Point2D MemoryManager::VisualCue::getShotSpin() {
	ADDRESS _spinObject = read<ADDRESS>(spinObject);
	Point2D spin = read<Point2D>(_spinObject + Offsets::VisualCue::Spin);
	if (spin.isZero()) {
		return spin;
	}
	double cueSpin = CueProperties::getCueSpin();
	return {
        NumberUtils::truncateTo4Places(spin.x) * cueSpin,
        NumberUtils::truncateTo4Places(spin.y) * cueSpin
	};
}
