package com.xtremeclicker.pro.model

sealed class Action(val timestamp: Long) {
    data class Click(val x: Float, val y: Float, timestamp: Long) : Action(timestamp)
    data class Swipe(
        val startX: Float,
        val startY: Float,
        val endX: Float,
        val endY: Float,
        val duration: Long,
        timestamp: Long
    ) : Action(timestamp)
}
