// Created by Denys on 13.07.2023.

#pragma once

#include "../data/type/Point2D.h"
#include "../data/GameConstants.h"

using ADDRESS = unsigned int;

class MemoryManager {
private:
    // address of the libgame-BPM-GooglePlay-Gold-Release-Module-xxxx.so library in game's memory
    static ADDRESS gameModuleBase;
    static ADDRESS sharedGameManager;
    static ADDRESS sharedMenuManager;
    static ADDRESS sharedUserSettings;

    static inline ADDRESS findModuleBase(const char *moduleName);

    template<typename T>
    static T &read(ADDRESS address) {
        return *reinterpret_cast<T *>(address);
    }

    template<typename T>
    static void write(ADDRESS address, T value) {
        *reinterpret_cast<T *>(address) = value;
    }

public:
    static bool initialize();

    class GameManager {
    private:
        static ADDRESS gameStateManager;
        static ADDRESS gameRules;
        static ADDRESS gameMode;
        static ADDRESS table;

        static inline int getGameState();

    public:
        static void initialize(ADDRESS _sharedGameManager);

        static ADDRESS getGameRules();

        static bool isValidGameState();

        static int getGameMode();

        static ADDRESS getTable();

        static bool is9BallMode();

        static BallClassification getPlayerClassification();

    };

    class Balls {
    private:
        static int ballsCount;
        static ADDRESS ballsList;

        static inline ADDRESS getBallByIndex(int ballIndex);

    public:
        static void initializeBallsList();

        static int getBallsCount() { return ballsCount; }

        static Point2D getBallPosition(int ballIndex);

        static BallClassification getBallClassification(int ballIndex);

        static BallState getBallState(int ballIndex);

        static bool isBallOnTable(BallState ballState);
    };

    class VisualCue {
    private:
        static ADDRESS visualCue;
        static ADDRESS spinObject;

        static constexpr double DEFAULT_ANGLE = 0.0;
        static double DEFAULT_POWER;

    public:
        static void initialize(ADDRESS _sharedGameManager);

        static double getShotAngle();

        static double getShotPower();

        static void setShotPower(double power);

        static Point2D getShotSpin();

    };

    class MenuManager {
    private:
        static ADDRESS menuStateManager;

        static inline int getMenuState();

    public:
        static void initialize(ADDRESS _sharedMenuManager);

        static bool isInGame();

        static bool isInMenu();
    };

    class UserSettings {
    private:
        static ADDRESS wideGuideline;
    public:
        static void initialize(ADDRESS _sharedUserSettings);

        static void switchWideGuideline(bool isEnabled);
    };

    class CueProperties {
    private:
        static ADDRESS cuePropertiesMaxPower;
        static ADDRESS cuePropertiesSpin;
        static const double cuePower[14];
        static const double cueSpin[14];

    public:
        static void initialize(ADDRESS _gameModuleBase);

        static double getCuePower();

        static double getCueSpin();

        static void writeCuePropertiesToMemory();
    };

};