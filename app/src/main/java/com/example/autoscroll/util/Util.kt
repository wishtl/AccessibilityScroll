package com.example.autoscroll.util

import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

var is_use_swipe = false

object Util {
    fun getRandomLong(min: Long = 5, max: Long = 30): Long {
        return Random.nextLong(min, max)
    }

    fun delay(long: Long) {
        Thread.sleep(long * 1000)
    }

    fun hasNodeByText(nodeInfo: AccessibilityNodeInfo?, text: String): Boolean {
        if (nodeInfo != null) {
            val list = nodeInfo.findAccessibilityNodeInfosByText(text)
            return list?.size!! > 0
        }
        return false
    }

    fun setUseSwipe(b: Boolean) {
        is_use_swipe = b
    }
    fun getUseSwipe(): Boolean {
        return is_use_swipe
    }
}