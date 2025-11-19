package com.xtremeclicker.pro.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import android.util.Log
import android.view.MotionEvent
import com.xtremeclicker.pro.model.Action

class ClickerAccessibilityService : AccessibilityService() {

    companion object {
        var instance: ClickerAccessibilityService? = null
            private set
        private const val TAG = "XtremeClicker"
        private val mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "Xtreme Clicker Service conectado – 1ms engine pronta")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Não precisamos escutar eventos (só usamos para gestos)
    }

    override fun onInterrupt() {
        instance = null
        Log.d(TAG, "Serviço interrompido")
    }

    // Função principal: clique único em 1ms
    fun click(x: Float, y: Float) {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 1)) // 1ms!
            .build()

        dispatchGesture(gesture, null, null)
    }

    // Multi-toque (até 10 dedos ao mesmo tempo)
    fun multiClick(points: List<Point>) {
        if (points.isEmpty()) return

        val strokes = points.map { point ->
            val path = Path().apply { moveTo(point.x.toFloat(), point.y.toFloat()) }
            GestureDescription.StrokeDescription(path, 0, 1)
        }

        val gesture = GestureDescription.Builder()
        strokes.forEach { gesture.addStroke(it) }
        dispatchGesture(gesture.build(), null, null)
    }

    // Swipe ultra-rápido ou lento (duração em ms)
    fun swipe(startX: Float, startY: Float, endX: Float, endY: Float, duration: Long = 50) {
        val path = Path().apply {
            moveTo(startX, startY)
            lineTo(endX, endY)
        }
        val stroke = GestureDescription.StrokeDescription(path, 0, duration)
        val gesture = GestureDescription.Builder()
            .addStroke(stroke)
            .build()

        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d(TAG, "Swipe concluído em ${duration}ms")
            }
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.w(TAG, "Swipe cancelado")
            }
        }, null)
    }

    // === GRAVADOR DE AÇÕES (converte toque real em código) ===
    private val recordedActions = mutableListOf<Action>()
    private var recordingStartTime = 0L

    fun startRecording() {
        recordedActions.clear()
        recordingStartTime = System.currentTimeMillis()
        Log.d(TAG, "Gravando ações...")
    }

    fun recordClick(x: Float, y: Float) {
        if (recordingStartTime == 0L) return
        val timestamp = System.currentTimeMillis() - recordingStartTime
        recordedActions.add(Action.Click(x, y, timestamp))
    }

    fun recordSwipe(startX: Float, startY: Float, endX: Float, endY: Float, duration: Long) {
        if (recordingStartTime == 0L) return
        val timestamp = System.currentTimeMillis() - recordingStartTime
        recordedActions.add(Action.Swipe(startX, startY, endX, endY, duration, timestamp))
    }

    fun stopRecording(): List<Action> {
        recordingStartTime = 0L
        Log.d(TAG, "Gravação finalizada – ${recordedActions.size} ações capturadas")
        return recordedActions.toList()
    }

    // === REPRODUTOR DE SCRIPT (loop infinito ou com delay) ===
    private var isRepeating = false
    private var currentRepeatTask: Runnable? = null

    fun playScript(actions: List<Action>, loop: Boolean = true, speedMultiplier: Float = 1f) {
        if (actions.isEmpty() || isRepeating) return
        isRepeating = true

        fun executeNext(index: Int) {
            if (!isRepeating || index >= actions.size) {
                if (loop && isRepeating) executeNext(0)
                return
            }

            val action = actions[index]
            val delay = if (index == 0) 0L else (actions[index].timestamp - actions[index - 1].timestamp) / speedMultiplier.toLong()

            mainHandler.postDelayed({
                when (action) {
                    is Action.Click -> click(action.x, action.y)
                    is Action.Swipe -> swipe(action.startX, action.startY, action.endX, action.endY, (action.duration / speedMultiplier).toLong())
                }
                if (isRepeating) executeNext(index + 1)
            }, delay.coerceAtLeast(1L)) // nunca menos de 1ms
        }

        executeNext(0)
    }

    fun stopPlayback() {
        isRepeating = false
        currentRepeatTask?.let { mainHandler.removeCallbacks(it) }
        Log.d(TAG, "Reprodução parada")
    }
}
