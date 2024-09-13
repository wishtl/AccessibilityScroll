package com.example.autoscroll.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.autoscroll.util.Config.TAG

class Gesture {
    companion object {

    }

    /**无障碍服务, 用于执行手势*/
    var service: AccessibilityService? = null
    //是否已经有手势在执行
    private var isDoing: Boolean = false
    private var gestureResultCallback: AccessibilityService.GestureResultCallback? = null

    init {
        gestureResultCallback = object : AccessibilityService.GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.d(TAG, "onCancelled: ")
                clear()
            }
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d(TAG, "onCompleted: ")
                clear()
            }
        }
    }

    fun clear() {
        isDoing = false
    }

    fun swipe(startX: Long, startY: Long, endX: Long, endY: Long, duration: Long=200L): Boolean? {
        Log.d(TAG, "swipe: startX: $startX, startY: $startY, endX: $endX, endY: $endY")
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(
               Path().apply {
                   moveTo(startX.toFloat(), startY.toFloat())
                   lineTo(endX.toFloat(), endY.toFloat())
               }, 0, duration))
        return service?.dispatchGesture(gestureBuilder.build(), gestureResultCallback, null)
    }

    fun click(x: Int, y: Int, delay: Long = 50L): Boolean? {
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(
                Path().apply {
                    moveTo(x.toFloat(), y.toFloat())
                }, 0, delay))
        return service?.dispatchGesture(gestureBuilder.build(), gestureResultCallback, null)
    }

    fun longClick(x: Int, y: Int, delay: Long = 1000L): Boolean? {
        return click(x, y, delay)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun doubleClick(x: Int, y: Int, delay: Long = 200L): Boolean? {
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(
            Path().apply {
                moveTo(x.toFloat(), y.toFloat())
            }, 0, delay, true))
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(
            Path().apply {
                moveTo(x.toFloat() + 5.0f, y.toFloat() + 5.0f)
            }, delay + 50L, delay))
        return  service?.dispatchGesture(gestureBuilder.build(), gestureResultCallback, null)
    }
}