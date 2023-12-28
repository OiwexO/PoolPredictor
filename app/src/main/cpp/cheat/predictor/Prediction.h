#pragma once

#include "../data/GameConstants.h"
#include "../data/type/Vector3D.h"
#include "../data/type/Point2D.h"
#include <vector>

class Prediction {
public:
    static bool pocketStatus[TABLE_POCKETS_COUNT];
//    Prediction() : espDataSize(0), espData(std::vector<float>()), guiData() {}
    Prediction() {}
    ~Prediction() {}

    std::vector<float>& getEspData();
    bool predictShotResult();

    bool mockPredictShotResult();

    class Ball {
    public:
        int index; // ball index 0..15
        BallClassification classification;
        BallState state;
        bool originalOnTable;
        bool onTable;

        Point2D velocity;
        Vector3D spin;
        Point2D initialPosition;
        Point2D predictedPosition;
        std::vector<Point2D> positions;

        void findNextCollision(void* pData, double* time);
        void calcVelocity();
        void calcVelocityPostCollision(const double& angle);
        void move(const double& time);
        bool isMovingOrSpinning() const;

        Ball() : index(0), classification(BallClassification::ERR_CLASSIFICATION), state(BallState::ERR_STATE),
                 originalOnTable(false), onTable(false), velocity(Point2D()), spin(Vector3D()),
                 initialPosition(Point2D()), predictedPosition(Point2D()), positions(std::vector<Point2D>()) {}

        ~Ball() {}

    private:
        // sub_1C29FA0 5.8.0
        bool isBallBallCollision(double* smallestTime, Prediction::Ball& otherBall) const;
        
        // sub_1BF9ADC 5.8.0
        bool willCollideWithTable(const double* smallestTime) const;

        // sub_1BF9BD8 5.8.0 determines if it's a collision with line or point
        void determineBallTableCollision(void* pData, double* smallestTime);

        // sub_1BC216C 5.8.0
        bool isBallLineCollision(double* pTime_1, const Point2D& tableShapePointA, const Point2D& tableShapePointB) const;

        // sub_1C2A594 5.8.0
        bool isBallPointCollision(double* smallestTime, const Point2D& tableShapePoint) const;

        // unused in 5.8.0 EV
        //bool sub_1C2A2C0(double* smallestTime, const Point2D* pointA, const Point2D* pointB);
        
        // sub_1B53EFC 5.8.0 - checks points received from sub_1BF9ADC, removed for optimization
        //bool sub_1B53EFC(const double* a1, const double* a2, const double* a3, const double* a4);

    };

    class Collision {
    public:
        Collision() : valid(false), type(Type::POINT), angle(0.0), point{}, ballA(nullptr), ballB(nullptr), firstHitBall(nullptr) {}
        ~Collision() {}

        enum Type : int {
            BALL,
            LINE,
            POINT
        };

        bool valid;
        Type type;
        double angle;
        Point2D point;
        //std::pair<int, int> collisionIndex;
        Prediction::Ball* ballA;
        Prediction::Ball* ballB;
        Prediction::Ball* firstHitBall;

    };

    class SceneData {
    public:
        int ballsCount;
        Ball balls[MAX_BALLS_COUNT];

        Collision collision;
        bool shotState;
        //bool validCushionShot;
        //bool forceCushionMode;
        //std::array<bool, 6> activeCushions;

        SceneData() : ballsCount(0), balls{}, collision{}, shotState(false) {}
        ~SceneData() {}

    } guiData;

private:
    unsigned int espDataSize = 0;

    std::vector<float> espData;

    void calculateEspDataSize();

    // initializes balls' position, shotState, classification etc
    void initBalls();

    // initializes balls with mock data for debug
    void mockInitBalls();

    void predictFinalPositions();

    void handleCollision();
    
    void handleBallBallCollision() const;

    void determineShotState();
    
};

extern Prediction* gPrediction;