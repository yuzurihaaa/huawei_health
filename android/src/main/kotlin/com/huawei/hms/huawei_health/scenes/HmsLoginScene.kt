package com.huawei.hms.huawei_health.scenes

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.huawei.hms.hihealth.data.Scopes
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import java.util.*

private const val TAG = "HmsLoginScene"
const val REQUEST_CODE = 8888

class HmsLoginScene(private val activity: Activity, result: MethodChannel.Result) : PluginRegistry.ActivityResultListener {

    fun hmsLogin() {
        Log.i(TAG, "on onLoginClick click")
        val scopeList: MutableList<Scope?> = ArrayList()
        val allRequiredScopes = arrayOfNulls<Scope>(Scopes.getMaxScopes().size)
        for (index in allRequiredScopes.indices) {
            allRequiredScopes[index] = Scope(Scopes.getMaxScopes()[index])
        }
        Collections.addAll(scopeList, *allRequiredScopes)
        scopeList.add(HuaweiIdAuthAPIManager.HUAWEIID_BASE_SCOPE)
        Log.i(TAG, "onRequestPermissionClick allRequiredScopes:" + allRequiredScopes.contentToString())
        val authParamsHelper = HuaweiIdAuthParamsHelper()
        val authParams = authParamsHelper.setAccessToken().setScopeList(scopeList).createParams()
        val authService = HuaweiIdAuthManager.getService(activity, authParams)
        val authHuaweiIdTask = authService.silentSignIn()
        authHuaweiIdTask.addOnSuccessListener { huaweiId: AuthHuaweiId? ->
            if (huaweiId == null) {
                Log.i(TAG, "huaweiId is null")
                return@addOnSuccessListener
            }
            Log.i(TAG, "silentSignIn success")
        }.addOnFailureListener { e: Exception? ->
            val signInIntent = authService.signInIntent

            activity.startActivityForResult(signInIntent, REQUEST_CODE, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.i(TAG, "requestCode $requestCode")
        Log.i(TAG, "resultCode $resultCode")
        return true;
    }
}