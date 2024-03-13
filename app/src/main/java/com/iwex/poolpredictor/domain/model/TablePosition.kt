package com.iwex.poolpredictor.domain.model

data class TablePosition(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {

    companion object {

        val DEFAULT = TablePosition(
            left = 0f,
            top = 0f,
            right = 0f,
            bottom = 0f
        )
    }
}
