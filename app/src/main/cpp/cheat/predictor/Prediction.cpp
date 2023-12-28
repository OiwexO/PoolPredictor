#include "Prediction.h"

#include "../memory/MemoryManager.h"
#include "../data/GlobalSettings.h"
#include "../data/table/TableProperties.h"
#include "../../utils/NumberUtils.h"
#include <array>

static Prediction prediction;
Prediction* gPrediction = &prediction;

bool Prediction::pocketStatus[] = {};

static double prevAngle = 0.0;
static double prevPower = 0.0;
static Point2D prevSpin = { 0.0, 0.0 };

constexpr double unk_35B7A20 = MIN_TIME;
constexpr double unk_35B3F70 = 1.5;
constexpr double unk_35B3F78 = 120.0;
constexpr double unk_35B3F80 = MIN_TIME;
//constexpr double dword_35B7988 = 0.54;
//constexpr double dword_35B7978 = 0.804;



/* PREDICTION PUBLIC METHODS ==================================================================== */

float* Prediction::getEspData() {
    this->calculateEspDataSize();
    float* espData = new float [espDataSize];
    int index = 0;
    constexpr int nOfBallsIndex = 1;
    espData[index++] = (float) GlobalSettings::isDrawLinesEnabled;
    if (GlobalSettings::isDrawLinesEnabled) {
        espData[index++] = 0.0f;
        for (int i = 0; i < guiData.ballsCount; i++) {
            Ball& ball = this->guiData.balls[i];
            if (ball.initialPosition != ball.predictedPosition) {
                espData[nOfBallsIndex] += 1.0f; // include only balls that changed their positions
                espData[index++] = (float) ball.index;
                espData[index++] = (float) ball.positions.size();
                for (auto& position : ball.positions) {
                    ScreenPoint point = position.toScreen();
                    espData[index++] = point.x;
                    espData[index++] = point.y;
                }
            }
        }
    }
    espData[index++] = (float) GlobalSettings::isDrawShotStateEnabled;
    if (GlobalSettings::isDrawShotStateEnabled) {
        for (bool _pocketStatus : Prediction::pocketStatus) {
            espData[index++] = (float) (_pocketStatus && this->guiData.shotState);
        }
    }
    return espData;
}

void Prediction::calculateEspDataSize() {
    espDataSize = 2; // isTrajectoryEnabled, nOfBalls
    if (GlobalSettings::isDrawLinesEnabled) {
        int ballsCount = this->guiData.ballsCount;
        for (int i = 0; i < ballsCount; i++) {
            Ball& ball = this->guiData.balls[i];
            if (ball.initialPosition != ball.predictedPosition) {
                // ballX, ballY, nOfBalls, ballIndex, nOfBallPositions
                espDataSize += (int) ball.positions.size() * 2 + 2;
            }
        }
        espDataSize++;
    }
    if (GlobalSettings::isDrawShotStateEnabled) {
        espDataSize += TABLE_POCKETS_COUNT;
    }
}

bool Prediction::predictShotResult() {
    double shotAngle = MemoryManager::VisualCue::getShotAngle();
    double shotPower = MemoryManager::VisualCue::getShotPower();
    Point2D shotSpin = MemoryManager::VisualCue::getShotSpin();
    if (shotAngle == prevAngle && shotPower == prevPower && shotSpin == prevSpin) {
        return false;
    }
    prevAngle = shotAngle;
    prevPower = shotPower;
    prevSpin.x = shotSpin.x;
    prevSpin.y = shotSpin.y;
    this->initBalls();
    double angleSin = sin(shotAngle);
    double angleCos = cos(shotAngle);
    Ball& cueBall = this->guiData.balls[0];
    cueBall.velocity.x = shotPower * angleCos;
    cueBall.velocity.y = shotPower * angleSin;
    double spinFactor = shotPower / BALL_RADIUS;
    double v31 = -shotSpin.y * spinFactor;
    cueBall.spin.x = -(angleSin * v31);
    cueBall.spin.y = angleCos * v31;
    cueBall.spin.z = shotSpin.x * spinFactor;
    this->guiData.collision.firstHitBall = nullptr;
    for (bool & _pocketStatus : pocketStatus) {
        _pocketStatus = false;
    }
    this->predictFinalPositions();
    if (GlobalSettings::isDrawShotStateEnabled) {
        this->determineShotState();
    }
    for (int i = 0; i < this->guiData.ballsCount; i++) {
        Ball& ball = this->guiData.balls[i];
        if (ball.positions.back() != ball.predictedPosition) {
            ball.positions.push_back(ball.predictedPosition);
        }
    }
    return true;
}

/* ============================================================================================== */



/* PREDICTION PRIVATE METHODS =================================================================== */

void Prediction::initBalls() {
    MemoryManager::Balls::initializeBallsList();
    this->guiData.ballsCount = MemoryManager::Balls::getBallsCount();
    for (int i = 0; i < this->guiData.ballsCount; i++) {
        Ball& ball = this->guiData.balls[i];
        ball.index = i;
        ball.state = MemoryManager::Balls::getBallState(i);
        ball.originalOnTable = MemoryManager::Balls::isBallOnTable(ball.state);
        ball.onTable = ball.originalOnTable;
        ball.classification = MemoryManager::Balls::getBallClassification(i);
        ball.initialPosition = MemoryManager::Balls::getBallPosition(i);
        ball.predictedPosition = ball.initialPosition;
        ball.velocity.nullify();
        ball.spin.nullify();
        if (!ball.positions.empty()) {
            ball.positions.clear();
        }
        ball.positions.reserve(20);
        ball.positions.push_back(ball.initialPosition);
    }
}

void Prediction::predictFinalPositions() {
    int i;
    bool isAnyBallMovingOrSpinning;
    double time;
    double time2;
    do {
        time = TIME_PER_TICK;
        do {
            time2 = time;
            this->guiData.collision.valid = false;
            // find the next collision for each ball
            for (i = 0; i < this->guiData.ballsCount; i++) {
                Ball& ball = this->guiData.balls[i];
                if (ball.onTable) {
                    ball.findNextCollision(&this->guiData, &time2);
                }
            }
            // move all balls to their collision positions
            for (i = 0; i < this->guiData.ballsCount; i++) {
                Ball& ball = this->guiData.balls[i];
                if (ball.onTable && ball.isMovingOrSpinning()) {
                    ball.move(time2);
                }
            }
            if (this->guiData.collision.valid) {
                this->handleCollision();
            }
            time -= time2;
        } while (time > MIN_TIME);
        isAnyBallMovingOrSpinning = false;
        for (i = 0; i < this->guiData.ballsCount; i++) {
            Ball& ball = this->guiData.balls[i];
            if (ball.onTable) {
                ball.calcVelocity();
                if (ball.isMovingOrSpinning()) {
                    isAnyBallMovingOrSpinning = true;
                }
            }
        }
    } while (isAnyBallMovingOrSpinning);
}

void Prediction::handleCollision() {
    Ball& ballA = *(this->guiData.collision.ballA);
    Ball& ballB = *(this->guiData.collision.ballB);
    if (!GlobalSettings::isPreciseTrajectoriesEnabled) {
        ballA.positions.push_back(ballA.predictedPosition);
    }
    switch (this->guiData.collision.type) {
    case Collision::Type::BALL:
        this->handleBallBallCollision();
        if (!GlobalSettings::isPreciseTrajectoriesEnabled) {
            ballB.positions.push_back(ballB.predictedPosition);
        }
        if (this->guiData.collision.firstHitBall == nullptr)
            this->guiData.collision.firstHitBall = &ballB;
        break;
    case Collision::Type::LINE:
        ballA.calcVelocityPostCollision(this->guiData.collision.angle);
        break;
    default:
        Point2D delta = {
                this->guiData.collision.point.y - ballA.predictedPosition.y,
            -(this->guiData.collision.point.x - ballA.predictedPosition.x)
        };
            this->guiData.collision.angle = - NumberUtils::calcAngle(delta);
            ballA.calcVelocityPostCollision(this->guiData.collision.angle);
        break;
    }
}

void Prediction::handleBallBallCollision() const {
    Ball& ballA = *(this->guiData.collision.ballA);
    Ball& ballB = *(this->guiData.collision.ballB);
    Point2D relativePosition = ballA.predictedPosition - ballB.predictedPosition;
    double invDistance = 1.0 / sqrt(relativePosition.square());
    Point2D collisionNormal = relativePosition * invDistance;
    double velocityComponentA = ballA.velocity.x * collisionNormal.x + ballA.velocity.y * collisionNormal.y;
    double velocityComponentB = ballB.velocity.x * collisionNormal.x + ballB.velocity.y * collisionNormal.y;
    Point2D velocityA = collisionNormal * velocityComponentA;
    Point2D velocityB = collisionNormal * velocityComponentB;
    ballA.velocity.x = velocityB.x - (velocityA.x - ballA.velocity.x);
    ballA.velocity.y = velocityB.y - (velocityA.y - ballA.velocity.y);
    ballB.velocity.x = velocityA.x - (velocityB.x - ballB.velocity.x);
    ballB.velocity.y = velocityA.y - (velocityB.y - ballB.velocity.y);
}

void Prediction::determineShotState() {
    this->guiData.shotState = false;
    // cue ball didn't hit any other ball
    if (this->guiData.collision.firstHitBall == nullptr) {
        return;
    }
    // cue ball potted
    if (!this->guiData.balls[0].onTable) {
        return;
    }
    BallClassification myClassification = MemoryManager::GameManager::getPlayerClassification(
            MemoryManager::GameManager::isPlayerTurn()
            );
    // 8-ball before break
    if (myClassification == BallClassification::ANY) {
        if (this->guiData.collision.firstHitBall->classification == BallClassification::EIGHT_BALL) {
            return;
        }
        for (int i = 0; i < this->guiData.ballsCount; i++) {
            Ball& ball = this->guiData.balls[i];
            // any ball except 8-ball has been potted during current shot
            if (ball.originalOnTable != ball.onTable) {
                this->guiData.shotState = this->guiData.balls[8].onTable;
                return;
            }
        }
    }
    else {
        //after break
        if (this->guiData.collision.firstHitBall->classification != myClassification) {
            return;
        }
        //9-ball mode
        if (myClassification == BallClassification::NINE_BALL_RULE) {
            for (int i = 1; i < this->guiData.ballsCount; i++) {
                Ball& ball = this->guiData.balls[i];
                // ball has been potted during current shot
                if (ball.originalOnTable != ball.onTable) {
                    this->guiData.shotState = true;
                    return;
                }
            }
            return;
        }
    }
    //8-ball mode after break
    if (myClassification == BallClassification::EIGHT_BALL) {
        // 8-ball has been potted during current shot
        this->guiData.shotState = !this->guiData.balls[8].onTable;
        return;
    }
    // to only check balls with correct classification
    int startBall = (myClassification == BallClassification::SOLID) ? 1 : 9;
    for (int i = startBall; i < startBall + 7; i++) {
        Ball& ball = this->guiData.balls[i];
        // any ball except 8-ball has been potted during current shot
        if (ball.originalOnTable != ball.onTable) {
            this->guiData.shotState = this->guiData.balls[8].onTable;
            return;
        }
    }
}

/* ============================================================================================== */



/* MOCK DATA METHODS =============================================================================*/

static int angleStep = 0;

bool Prediction::mockPredictShotResult() {
    double shotAngle = (angleStep++ % 362) * MIN_ANGLE_STEP_RADIANS;
    double shotPower = 888.85;
    Point2D shotSpin = {0.5, 0.5};
    this->mockInitBalls();
    double angleSin = sin(shotAngle);
    double angleCos = cos(shotAngle);
    Ball& cueBall = this->guiData.balls[0];
    cueBall.velocity.x = shotPower * angleCos;
    cueBall.velocity.y = shotPower * angleSin;
    double spinFactor = shotPower / BALL_RADIUS;
    double v31 = -shotSpin.y * spinFactor;
    cueBall.spin.x = -(angleSin * v31);
    cueBall.spin.y = angleCos * v31;
    cueBall.spin.z = shotSpin.x * spinFactor;
    this->guiData.collision.firstHitBall = nullptr;
    for (bool & _pocketStatus : pocketStatus) {
        _pocketStatus = false;
    }
    this->predictFinalPositions();
    for (int i = 0; i < this->guiData.ballsCount; i++) {
        Ball& ball = this->guiData.balls[i];
        if (ball.positions.back() != ball.predictedPosition) {
            ball.positions.push_back(ball.predictedPosition);
        }
    }
    return true;
}

void Prediction::mockInitBalls() {
    this->guiData.ballsCount = 16;
    const Point2D ballPositions[MAX_BALLS_COUNT] = {
            Point2D(-63.5, 0),
            Point2D(83.3017, -11.499),
            Point2D(83.2816, -3.85494),
            Point2D(89.8739, -0.0383624),
            Point2D(89.8901, -7.68204),
            Point2D(76.6703, 7.59622),
            Point2D(89.9122, -15.3189),
            Point2D(70.077, -3.85576),
            Point2D(76.6635, -0.034193),
            Point2D(76.6835, -7.67734),
            Point2D(63.4656, -0.037683),
            Point2D(83.2833, 11.4105),
            Point2D(70.0569, 3.77762),
            Point2D(89.8887, 15.2391),
            Point2D(83.2555, 3.77997),
            Point2D(89.8751, 7.5909)
    };
    const BallClassification ballClassifications[MAX_BALLS_COUNT] = {
            BallClassification::CUE_BALL,
            BallClassification::SOLID,
            BallClassification::SOLID,
            BallClassification::SOLID,
            BallClassification::SOLID,
            BallClassification::SOLID,
            BallClassification::SOLID,
            BallClassification::SOLID,
            BallClassification::EIGHT_BALL,
            BallClassification::STRIPE,
            BallClassification::STRIPE,
            BallClassification::STRIPE,
            BallClassification::STRIPE,
            BallClassification::STRIPE,
            BallClassification::STRIPE,
            BallClassification::STRIPE
    };
    for (int i = 0; i < this->guiData.ballsCount; i++) {
        Ball& ball = this->guiData.balls[i];
        ball.index = i;
        ball.state = BallState::DEFAULT;
        ball.originalOnTable = true;
        ball.onTable = ball.originalOnTable;
        ball.classification = ballClassifications[i];
        ball.initialPosition = ballPositions[i];
        ball.predictedPosition = ball.initialPosition;
        ball.velocity.nullify();
        ball.spin.nullify();
        if (!ball.positions.empty())
            ball.positions.clear();
        ball.positions.reserve(20);
        ball.positions.push_back(ball.initialPosition);
    }
}

/* ============================================================================================== */



/* BALL PUBLIC METHODS ========================================================================== */

void Prediction::Ball::findNextCollision(void* pData, double* time) {
    auto* data = reinterpret_cast<SceneData*>(pData);
    auto pockets = TableProperties::getPockets();
    // find collisions with other balls
    if (this->state == BallState::DEFAULT) {
        for (int i = this->index + 1; i < data->ballsCount; i++) {
            Ball& otherBall = data->balls[i];
            if (otherBall.state == BallState::DEFAULT && this->isBallBallCollision(time, otherBall)) {
                data->collision.valid = true;
                data->collision.ballA = this;
                data->collision.type = Collision::Type::BALL;
                data->collision.ballB = &otherBall;
            }
        }
    }
    if (this->willCollideWithTable(time)) {
        if (this->state == BallState::IN_POCKET) {
            double unkTime = *time * unk_35B3F70;
            this->velocity.x -= this->predictedPosition.x * unkTime;
            this->velocity.y -= this->predictedPosition.y * unkTime;
        }
        // check if this ball is potted
        else if (this->state == BallState::DEFAULT) {
            double deltaSquare;
            double unkTime;
            Point2D delta;
            for (int i = 0; i < TABLE_POCKETS_COUNT; i++) {
                delta.x = pockets[i].x - this->predictedPosition.x;
                delta.y = pockets[i].y - this->predictedPosition.y;
                deltaSquare = delta.x * delta.x + delta.y * delta.y;
                if (deltaSquare < POCKET_RADIUS_SQUARE) {
                    unkTime = *time * unk_35B3F78;
                    this->velocity.x += delta.x * unkTime;
                    this->velocity.y += delta.y * unkTime;
                    if (deltaSquare < BALL_RADIUS_SQUARE) {
                        this->state = BallState::IN_POCKET;
                        Prediction::pocketStatus[i] = true;
                    }
                }
            }
        }
        this->determineBallTableCollision(pData, time);
    }
    if (this->state == BallState::IN_POCKET) {
        this->state = BallState::UNKNOWN;
        this->onTable = false;
        this->velocity.nullify();
        this->spin.nullify();
    }
}

void Prediction::Ball::calcVelocity() {
    if (!this->isMovingOrSpinning()) {
        return;
    }
    double v15 = BALL_RADIUS * this->spin.x - this->velocity.y;
    double v16 = -this->velocity.x - this->spin.y * BALL_RADIUS;
    double v17 = sqrt(v16 * v16 + v15 * v15);
    double v18 = v17 * 0.00145772594752187;
    if (v18 > unk_35B3F80) {
        double v20 = (v18 < TIME_PER_TICK) ? (v17 * 0.00145772594752187) : TIME_PER_TICK;
        double v21 = 196.0 * v20 / v17;
        double v22 = v16 * v21;
        double v23 = v15 * v21;
        this->velocity.x += v22;
        this->velocity.y += v23;
        this->spin.x -= v23 * 0.6578125102783204; // unk_35B3F88 / BALL_RADIUS
        this->spin.y += v22 * 0.6578125102783204; // unk_35B3F88 / BALL_RADIUS
    }
    if (v18 < TIME_PER_TICK) {
        double v24 = this->velocity.x;
        double v25 = this->velocity.y;
        double v27 = (TIME_PER_TICK - v18) * 10.878;
        double v28 = 1.0 - v27 / sqrt(v25 * v25 + v24 * v24);
        v28 = (v28 < 0.0) ? 0.0 : v28;
        this->velocity.x = v24 * v28;
        this->velocity.y = v25 * v28;
        this->spin.x = v25 * v28 / BALL_RADIUS;
        this->spin.y = -(v24 * v28) / BALL_RADIUS;
    }
    constexpr double v29 = 9.8 * TIME_PER_TICK;
    this->spin.z = (this->spin.z > 0.0) ? fmax(this->spin.z - v29, 0.0) : fmin(this->spin.z + v29, 0.0);

}

void Prediction::Ball::calcVelocityPostCollision(const double& angle) {
    double angleCos = cos(angle);
    double angleSin = sin(angle);
    double velocityX = angleCos * this->velocity.x - angleSin * this->velocity.y;
    double velocityY = angleSin * this->velocity.x + angleCos * this->velocity.y;
    double spinFactor = velocityX - BALL_RADIUS * this->spin.z;
    double absSpinFactor = (spinFactor > 0.0) ? spinFactor : -spinFactor;
    double velocityFactor = absSpinFactor / 2.5;
    double absVelocityY = (velocityY > 0.0) ? velocityY : -velocityY;
    double spinDirection = (spinFactor > 0.0) ? 1.0 : -1.0;
    double minSpinFactor = 0.4 * absVelocityY;
    if (velocityFactor < minSpinFactor) {
        minSpinFactor = velocityFactor;
    }
    double spinChange = spinDirection * minSpinFactor;
    double newVelocityX = velocityX - spinChange / 2.5;
    double newVelocityY = -0.804 * velocityY; // -(velocityY * dword_35B7978)
    this->velocity.x = angleSin * newVelocityY + angleCos * newVelocityX;
    this->velocity.y = angleCos * newVelocityY - newVelocityX * angleSin;
    double newSpinX = angleSin * this->spin.x + angleCos * this->spin.y;
    double newSpinY = angleCos * this->spin.x - angleSin * this->spin.y - velocityY * 0.1420875022201172; // dword_35B7988 / BALL_RADIUS
    double newSpinZ = this->spin.z + spinChange * 0.6578125102783204; // unk_35B7A28 / BALL_RADIUS
    this->spin.x = angleSin * newSpinX + angleCos * newSpinY;
    this->spin.y = angleCos * newSpinX - newSpinY * angleSin;
    this->spin.z = newSpinZ;
}

void Prediction::Ball::move(const double& time) {
    if (!this->velocity.isZero()) {
        this->predictedPosition.x += this->velocity.x * time;
        this->predictedPosition.y += this->velocity.y * time;
        if (GlobalSettings::isPreciseTrajectoriesEnabled) {
            int lastIndex = this->positions.size() - 1;
            if (lastIndex > 1) {
                Point2D &a = this->positions[lastIndex - 1];
                Point2D &b = this->positions[lastIndex];
                Point2D &c = this->predictedPosition;
                if (((b.y - a.y) * (c.x - b.x)) == ((c.y - b.y) * (b.x - a.x))) {
                    return;
                }
            }
            this->positions.push_back(this->predictedPosition);
        }
    }
}

bool Prediction::Ball::isMovingOrSpinning() const {
    return this->velocity.isNotZero() || this->spin.isNotZero();
}

/* ============================================================================================== */



/* BALL PRIVATE METHODS */

bool Prediction::Ball::isBallBallCollision(double* smallestTime, Prediction::Ball& otherBall) const {
    Point2D relativePosition = otherBall.predictedPosition - this->predictedPosition;
    Point2D velocityDelta = otherBall.velocity - this->velocity;
    double v24 = (relativePosition.x * velocityDelta.x + relativePosition.y * velocityDelta.y) * 2.0;
    if (v24 >= 0.0) {
        return false;
    }
    double velocityDeltaSquare = velocityDelta.square();
    double v27 = (relativePosition.square() - BALL_RADIUS_SQUARE * 4) * (velocityDeltaSquare * 4.0);
    double square = v24 * v24;
    if (square < v27) {
        return false;
    }
    double v28 = (-v24 - sqrt(square - v27)) / (velocityDeltaSquare * 2.0);
    if (v28 < 0.0) {
        return false;
    }
    if (v28 - unk_35B7A20 > *smallestTime) {
        return false;
    }
    *smallestTime = v28;
    return true;
}

bool Prediction::Ball::willCollideWithTable(const double* smallestTime) const {
    double currentX = this->predictedPosition.x;
    double currentY = this->predictedPosition.y;
    double predictedX = currentX + this->velocity.x * *smallestTime;
    double predictedY = currentY + this->velocity.y * *smallestTime;
    double leftX;
    double rightX;
    double bottomY;
    double topY;
    if (this->velocity.x > 0.0) {
        leftX = currentX;
        rightX = predictedX;
    }
    else {
        leftX = predictedX;
        rightX = currentX;
    }
    if (this->velocity.y > 0.0) {
        topY = currentY;
        bottomY = predictedY;
    }
    else {
        topY = predictedY;
        bottomY = currentY;
    }
    return (leftX < TABLE_BOUND_LEFT || rightX > TABLE_BOUND_RIGHT || topY < TABLE_BOUND_TOP || bottomY > TABLE_BOUND_BOTTOM);
}

void Prediction::Ball::determineBallTableCollision(void* pData, double* smallestTime) {
    double angle;
    auto* data = reinterpret_cast<Prediction::SceneData*>(pData);
    auto tableShape = TableProperties::getTableShape();
    for (int i = 0; i < TABLE_SHAPE_SIZE; i++) {
        const Point2D& point = tableShape[i];
        const Point2D& nextPoint = tableShape[(i + 1) % TABLE_SHAPE_SIZE];
        if (this->isBallLineCollision(smallestTime, point, nextPoint)) {
            angle = NumberUtils::calcAngle(nextPoint, point);
            data->collision.valid = true;
            data->collision.ballA = this;
            data->collision.type = Collision::Type::LINE;
            data->collision.angle = -angle;
        }
        else if (this->isBallPointCollision(smallestTime, point)) {
            data->collision.valid = true;
            data->collision.ballA = this;
            data->collision.point = point;
            data->collision.type = Collision::Type::POINT;
        }
    }
}

bool Prediction::Ball::isBallLineCollision(double* pTime_1, const Point2D& tableShapePointA, const Point2D& tableShapePointB) const {
    if (this->velocity.isZero()) {
        return false;
    }
    Point2D delta = tableShapePointB - tableShapePointA;
    double v17 = delta.y * this->velocity.x - delta.x * this->velocity.y;
    if (v17 == 0.0) {
        return false;
    }
    double invDistance = 1.0 / sqrt(delta.square());
    double v21 = invDistance * BALL_RADIUS;
    double v22 = this->predictedPosition.x - tableShapePointA.x - delta.y * v21;
    double v23 = this->predictedPosition.y - tableShapePointA.y + delta.x * v21;
    double v24 = (v22 * -this->velocity.y - v23 * -this->velocity.x) / v17;
    if (v24 <= 0.0 || v24 >= 1.0) {
        return false;
    }
    double time = (delta.x * v23 - delta.y * v22) / v17;
    if (time <= 0.0 || (time - 1E-11 > *pTime_1)) {
        return false;
    }
    if (this->velocity.x * (delta.y * invDistance) + this->velocity.y * -(delta.x * invDistance) > 0.0) {
        return false;
    }
    *pTime_1 = time;
    return true;
}

bool Prediction::Ball::isBallPointCollision(double* smallestTime, const Point2D& tableShapePoint) const {
    Point2D delta = tableShapePoint - this->predictedPosition;
    double v16 = -(this->velocity.x * delta.x * 2.0) - (this->velocity.y * delta.y * 2.0);
    if (v16 >= 0.0) {
        return false;
    }
    double velocitySquare = this->velocity.square();
    double distanceSquare = delta.square();
    double unkSquare = v16 * v16;
    if (distanceSquare - unkSquare / (velocitySquare * 4.0) >= BALL_RADIUS_SQUARE) {
        return false;
    }
    double v22 = (-v16 - sqrt(unkSquare - velocitySquare * 4.0 * (distanceSquare - BALL_RADIUS_SQUARE))) / (velocitySquare * 2.0);
    if (v22 < 0.0) {
        return false;
    }
    if (v22 - unk_35B7A20 > *smallestTime) {
        return false;
    }
    *smallestTime = v22;
    return true;
}
