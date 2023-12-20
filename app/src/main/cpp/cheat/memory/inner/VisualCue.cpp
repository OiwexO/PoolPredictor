// Created by Denys on 13.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"
#include "../../../utils/NumberUtils.h"

ADDRESS MemoryManager::VisualCue::visualCue = 0;
ADDRESS MemoryManager::VisualCue::spinObject = 0;
ADDRESS MemoryManager::VisualCue::cuePropertiesMaxPower = 0;
ADDRESS MemoryManager::VisualCue::cuePropertiesSpin = 0;
ADDRESS MemoryManager::VisualCue::cuePropertiesAccuracy = 0;

double MemoryManager::VisualCue::DEFAULT_POWER = 1.0;

void MemoryManager::VisualCue::initialize(ADDRESS _gameModuleBase, ADDRESS _sharedGameManager) {
	visualCue = _sharedGameManager + Offsets::CueProperties::VisualCue;
	spinObject = _sharedGameManager + Offsets::CueProperties::SpinObject;
	cuePropertiesMaxPower = _gameModuleBase + Offsets::CueProperties::CuePower;
	cuePropertiesSpin = _gameModuleBase + Offsets::CueProperties::CueSpin;
	cuePropertiesAccuracy = _gameModuleBase + Offsets::CueProperties::CueAccuracy;
}

double MemoryManager::VisualCue::getShotAngle() {
	ADDRESS _visualCue = read<ADDRESS>(visualCue);
	ADDRESS visualGuide = read<ADDRESS>(_visualCue + Offsets::CueProperties::VisualGuide);
    double angle = read<double>(visualGuide + Offsets::CueProperties::AimAngle);
	if (angle < 0.0 || angle > MAX_ANGLE_RADIANS) {
		return DEFAULT_ANGLE;
	}
	return NumberUtils::truncateTo4Places(angle);
}

double MemoryManager::VisualCue::getShotPower() {
	ADDRESS _visualCue = read<ADDRESS>(visualCue);
	double power = read<double>(_visualCue + Offsets::CueProperties::Power);
	if (power <= 0.0 || power > 1.0) {
		power = DEFAULT_POWER;
	}
	else {
		power = NumberUtils::truncateTo4Places(power);
	}
	double maxPower = read<double>(cuePropertiesMaxPower);
	if (maxPower <= 0.0) {
		return power;
	}
	return (1.0 - sqrt(1.0 - power)) * maxPower;
}

void MemoryManager::VisualCue::setShotPower(const double power) {
	ADDRESS _visualCue = read<ADDRESS>(visualCue);
	write(_visualCue + Offsets::CueProperties::Power, power);
}

Point2D MemoryManager::VisualCue::getShotSpin() {
	ADDRESS _spinObject = read<ADDRESS>(spinObject);
	Point2D spin = read<Point2D>(_spinObject + Offsets::CueProperties::Spin);
	if (spin.isZero()) {
		return spin;
	}
	double cueSpinStat = read<double>(cuePropertiesSpin);
	if (cueSpinStat <= 0.0) {
		return spin;
	}
	return {
		NumberUtils::truncateTo4Places(spin.x) * cueSpinStat,
		NumberUtils::truncateTo4Places(spin.y) * cueSpinStat
	};
}
