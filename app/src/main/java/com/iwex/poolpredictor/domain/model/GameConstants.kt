package com.iwex.poolpredictor.domain.model

const val NUMBER_OF_BALLS = 16
const val FIRST_STRIPE_BALL_INDEX = 9
const val NUMBER_OF_POCKETS = 6

fun isSolidBall(ballIndex: Int) = ballIndex < FIRST_STRIPE_BALL_INDEX