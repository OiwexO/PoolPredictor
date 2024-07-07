package com.iwex.poolpredictor.presentation.util

class StringUtils {
    companion object {
        fun formatStringWithNumber(str: String, value: Int) = String.format("$str: %d", value)
    }
}
