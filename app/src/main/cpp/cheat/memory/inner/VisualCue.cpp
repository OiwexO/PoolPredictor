// Created by Denys on 13.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"
#include "../../../utils/NumberUtils.h"
#include "../../data/GlobalSettings.h"

ADDRESS MemoryManager::VisualCue::visualCue = 0;
ADDRESS MemoryManager::VisualCue::spinObject = 0;
ADDRESS MemoryManager::VisualCue::cuePropertiesMaxPower = 0;
ADDRESS MemoryManager::VisualCue::cuePropertiesSpin = 0;
ADDRESS MemoryManager::VisualCue::cuePropertiesAccuracy = 0;

double MemoryManager::VisualCue::DEFAULT_POWER = 1.0;

/*
float MemoryManager::VisualCue::cuePower[] = {
		666.0,
		682.0,
		699.0,
		715.0,
		731.0,
		747.0,
		764.0,
		780.0,
		796.0,
		813.0,
		828.85,
		848.85,
		868.85,
		888.85
};

double MemoryManager::VisualCue::cueSpin[] = {
		0.37931035305,
		0.48275861247,
		0.58620691299,
		0.68965517241,
		0.71724135300,
		0.74482761580,
		0.77241379639,
		0.79999997698,
		0.82758623978,
		0.85517242037,
		0.88275860096,
		0.91034478155,
		0.93793104435,
		0.96551722494
};
*/

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
