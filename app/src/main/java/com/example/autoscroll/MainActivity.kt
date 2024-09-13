package com.example.autoscroll

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.CompoundButton
import android.widget.Switch
import androidx.activity.ComponentActivity
import com.example.autoscroll.util.Config.TAG
import com.example.autoscroll.util.Util.setUseSwipe


class MainActivity : ComponentActivity() , CompoundButton.OnCheckedChangeListener {

    private var use_swipe: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initListener()

        Log.d(TAG, "onCreate: ")

        if (!isServiceEnabled()) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        } else {
            Log.d(TAG, "onCreate: 服务已启用，可以开始自动滑屏")
            // 服务已启用，可以开始自动滑屏
        }
    }

    private fun isServiceEnabled():Boolean {
        val accessibilityManager = getSystemService(AccessibilityService.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (service in enabledServices) {
            Log.d(TAG, service.id)
            if (service.id.contains("com.example.autoscroll")) {
                return true
            }
        }
        return false
    }

    private fun initView() {
        use_swipe = findViewById<Switch>(R.id.swipe)
    }

    private fun initListener() {
        use_swipe!!.setOnCheckedChangeListener(this)
    }
    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        when (compoundButton.id) {
            R.id.swipe -> {
                setUseSwipe(b)
                use_swipe?.isChecked  = b
            }
        }
    }
}




