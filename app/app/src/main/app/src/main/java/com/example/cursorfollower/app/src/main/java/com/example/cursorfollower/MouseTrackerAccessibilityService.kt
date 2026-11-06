package com.example.cursorfollower

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import android.widget.ImageView

class MouseTrackerAccessibilityService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private lateinit var rootOverlayView: FrameLayout
    private lateinit var followerImageView: ImageView

    private val overlayParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        PixelFormat.TRANSLUCENT
    )

    override fun onServiceConnected() {
        super.onServiceConnected()

        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.DEFAULT or AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
            notificationTimeout = 16
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        rootOverlayView = FrameLayout(this)

        val sizePx = (48 * resources.displayMetrics.density).toInt()
        followerImageView = ImageView(this).apply {
            setImageResource(android.R.drawable.star_big_on)
            layoutParams = FrameLayout.LayoutParams(sizePx, sizePx)
            translationX = -200f
            translationY = -200f
        }
        rootOverlayView.addView(followerImageView)

        rootOverlayView.setOnHoverListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_MOVE, MotionEvent.ACTION_HOVER_ENTER -> {
                    val offset = 24 * resources.displayMetrics.density
                    followerImageView.translationX = event.rawX + offset
                    followerImageView.translationY = event.rawY + offset
                }
                MotionEvent.ACTION_HOVER_EXIT -> {
                    followerImageView.translationX = -200f
                    followerImageView.translationY = -200f
                }
            }
            false
        }

        overlayParams.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(rootOverlayView, overlayParams)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        if (::rootOverlayView.isInitialized) {
            windowManager.removeView(rootOverlayView)
        }
    }
}
