package com.iwex.poolpredictor.domain.model

const val NUMBER_OF_BALLS = 16
const val FIRST_STRIPE_BALL_INDEX = 9
const val NUMBER_OF_POCKETS = 6
const val MAX_CUE_POWER = 13
const val MAX_CUE_SPIN = 13

fun isSolidBall(ballIndex: Int) = ballIndex < FIRST_STRIPE_BALL_INDEX