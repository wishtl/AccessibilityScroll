package com.example.autoscroll.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.autoscroll.util.Config.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class AutoService : AccessibilityService() {
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var isAlipayRunning = false
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onInterrupt() {
        // 当服务被中断时执行的代码
        Log.d(TAG, "onInterrupt: ")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val serviceInfo = AccessibilityServiceInfo()
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo.packageNames = arrayOf(
            "com.eg.android.AlipayGphone", "io.dcloud.HBuilder"
        ) // 监控的app
        serviceInfo.notificationTimeout = 100
        serviceInfo.flags =
            serviceInfo.flags or AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
        setServiceInfo(serviceInfo);
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // 这里可以添加你需要在特定事件时执行的代码

        val eventType = event.eventType
        val packageName = event.packageName.toString()
        val className = event.className.toString()
        val alipayMonitor = AlipayMonitor().apply {
            service = this@AutoService
        }

        when (eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED, AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                Log.d(TAG, "onAccessibilityEvent: $eventType $packageName $className")
                if ("com.eg.android.AlipayGphone" == packageName) {
                    if (isAlipayRunning) return
                    isAlipayRunning = true
                    serviceScope.launch {
                        alipayMonitor.policyVideo(rootInActiveWindow,packageName, className)
                        isAlipayRunning = false
                    }
                }
            }
            else -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.d(TAG, "onDestroy: ")
    }

}