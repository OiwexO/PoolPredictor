package com.iwex.poolpredictor.domain.model

data class TablePosition(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {

    companion object {

        val DEFAULT = TablePosition(
            left = 0,
            top = 0,
            right = 0,
            bottom = 0
        )
    }
}
