package com.iwex.poolpredictor.domain.model.menu.tabs

data class EspTabState(
    val lineWidth: Int,
    val ballRadius: Int,
    val trajectoryOpacity: Int,
    val shotStateCircleWidth: Int,
    val shotStateCircleRadius: Int,
    val shotStateCircleOpacity: Int
) {

    companion object {

        val DEFAULT = EspTabState(
            lineWidth = 5,
            ballRadius = 21,
            trajectoryOpacity = 100,
            shotStateCircleWidth = 8,
            shotStateCircleRadius = 42,
            shotStateCircleOpacity = 100
        )
    }
}
