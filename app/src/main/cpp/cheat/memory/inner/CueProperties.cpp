// Created by Denys on 25.07.2023.

#include "../MemoryManager.h"
#include "../Offsets.h"

ADDRESS MemoryManager::CueProperties::cuePropertiesMaxPower = 0;
ADDRESS MemoryManager::CueProperties::cuePropertiesSpin = 0;

const double MemoryManager::CueProperties::cuePower[] = {
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

const double MemoryManager::CueProperties::cueSpin[] = {
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

void MemoryManager::CueProperties::initialize(ADDRESS _gameModuleBase) {
    cuePropertiesMaxPower = _gameModuleBase + Offsets::CueProperties::CuePower;
    cuePropertiesSpin = _gameModuleBase + Offsets::CueProperties::CueSpin;
}

double MemoryManager::CueProperties::getCuePower() {
    return read<double>(cuePropertiesMaxPower);
}

void MemoryManager::CueProperties::setCuePower(int level) {
    if (level < 0 || level > 13) {
        return;
    }
    double power = cuePower[level];
    write(cuePropertiesMaxPower, power);
}

double MemoryManager::CueProperties::getCueSpin() {
    return read<double>(cuePropertiesSpin);
}

void MemoryManager::CueProperties::setCueSpin(int level) {
    if (level < 0 || level > 13) {
        return;
    }
    double spinConstantValue = cueSpin[level];
    write(cuePropertiesSpin, spinConstantValue);
}
