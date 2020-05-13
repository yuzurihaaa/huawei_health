package com.huawei.hms.huawei_health

import android.app.Activity
import androidx.annotation.NonNull
import com.huawei.hms.huawei_health.scenes.getDistance
import com.huawei.hms.huawei_health.scenes.getSteps
import com.huawei.hms.huawei_health.scenes.hmsLogin
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** HuaweiHealthPlugin */
class HuaweiHealthPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val channel = MethodChannel(flutterPluginBinding.binaryMessenger, "huawei_health")
        channel.setMethodCallHandler(this)
    }


    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "huawei_health")
            channel.setMethodCallHandler(HuaweiHealthPlugin())
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "loginHuawei" -> {
                hmsLogin(activity, result)
            }
            "getSteps" -> {
                getSteps(call, activity, result)
            }
            "getDistance" -> {
                getDistance(call, activity, result)
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }
}
