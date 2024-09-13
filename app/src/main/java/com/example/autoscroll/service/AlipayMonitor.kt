package com.example.autoscroll.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.example.autoscroll.util.Config.TAG
import com.example.autoscroll.util.Gesture
import com.example.autoscroll.util.Util
import com.example.autoscroll.util.Util.getRandomLong
import com.example.autoscroll.util.Util.hasNodeByText


class AlipayMonitor {
    var service: AccessibilityService? = null

    // 视频界面
    fun policyVideo(nodeInfo: AccessibilityNodeInfo?, packageName: String, className: String) {
        // 是否在视频界面
        if (isVideoUI(nodeInfo)) {
            // 在视频中
            scrollVideo(nodeInfo)
        }
    }

    private fun isVideoUI(nodeInfo: AccessibilityNodeInfo?): Boolean {
        return hasNodeByText(nodeInfo, "喜欢就评论吧～")
    }

    // 滑动视频
    private fun scrollVideo(nodeInfo: AccessibilityNodeInfo?) {
        // 视频  延时15-35秒 刷新
        var randomLong = Util.getRandomLong(15, 35)
        if (findLiveNode(nodeInfo)) {
            // 直播  延时3-5秒 刷新
            randomLong = Util.getRandomLong(3, 5)
        }
        Log.d(TAG, "scrollVideo: randomLong = $randomLong")
        Util.delay(randomLong)
        //  执行滑动
        val isUseSwipe = Util.getUseSwipe()
        Log.d(TAG, "scrollVideo: isUseSwipe = $isUseSwipe")
        if (isUseSwipe) {
            moveUp()
        } else {
            findVideoNode(nodeInfo)
        }
    }

    private fun moveDown(): Boolean? {
        Gesture().apply {
            service = this@AlipayMonitor.service
            if (null == service) {
                return false
            }
            val screenWidth = service!!.resources.displayMetrics.widthPixels
            val screenHeight = service!!.resources.displayMetrics.heightPixels
            val startX = screenWidth / 2 + getRandomLong(0, 50)
            val startY = screenHeight / 2 + getRandomLong(0, 50)
            val endX = screenWidth / 2 + getRandomLong(0, 50)
            val endY = screenHeight / 2 + getRandomLong(200, 250)
            return swipe(startX, startY, endX, endY)
        }
    }

    private fun moveUp() {
        Gesture().apply {
            service = this@AlipayMonitor.service
            if (null == service) {
                return
            }
            val screenWidth = service!!.resources.displayMetrics.widthPixels
            val screenHeight = service!!.resources.displayMetrics.heightPixels
            val startX = screenWidth / 2 - getRandomLong(0, 50)
            val startY = screenHeight / 2 - getRandomLong(0, 50)
            val endX = screenWidth / 2 - getRandomLong(0, 50)
            val endY = screenHeight / 2 - getRandomLong(200, 250)
            swipe(startX, startY, endX, endY)
        }
    }


    private fun findVideoNode(nodeInfo: AccessibilityNodeInfo?) {
        if (nodeInfo != null && nodeInfo.childCount > 0) {
            for (i in 0 until nodeInfo.childCount) {
                val child = nodeInfo.getChild(i) ?: continue
                // 有时 child 为空
                val className = child.className.toString()
                if ("android.support.v7.widget.RecyclerView" == className && child.isScrollable) {
                    val result = child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    if (result) {
                        Log.d(TAG, "scrollVideo: success")
                    } else {
                        Log.d(TAG, "scrollVideo: fail")
                        child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
                    }
                }
                // 递归调用
                findVideoNode(child)
            }
        }
    }

    private fun findLiveNode(nodeInfo: AccessibilityNodeInfo?): Boolean {
        return hasNodeByText(nodeInfo, "点击进入直播间")
    }


}
