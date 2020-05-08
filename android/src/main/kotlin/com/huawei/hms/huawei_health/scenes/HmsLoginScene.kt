package com.huawei.hms.huawei_health.scenes

import android.app.Activity
import android.util.Log
import com.huawei.hihealthkit.HiHealthDataQuery
import com.huawei.hihealthkit.HiHealthDataQueryOption
import com.huawei.hihealthkit.auth.HiHealthAuth
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType
import com.huawei.hihealthkit.data.HiHealthPointData
import com.huawei.hihealthkit.data.store.HiHealthDataStore
import com.huawei.hihealthkit.data.type.HiHealthPointType
import io.flutter.plugin.common.MethodChannel


private const val TAG = "HmsLoginScene"

class HmsLoginScene(private val activity: Activity, result: MethodChannel.Result) {

    fun hmsLogin() {
        val read = intArrayOf(
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_STEP_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_DISTANCE_SUM)
        val write = intArrayOf()

        HiHealthAuth.requestAuthorization(activity, write, read) { p0, p1 ->
            Log.i(TAG, "requestAuthorization onResult: $p0")
            Log.i(TAG, "requestAuthorization onResult: $p1")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun query() {
        val timeout = 0
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000L * 60 * 60 * 24 * 30 // Check Data of the latest 5 days

// Get HiHealthPointType, like steps, distance, calories, exercise intensity per day.
// Statistic data returned as an ArrayList where each element represents the value of one day
// Get HiHealthPointType, like steps, distance, calories, exercise intensity per day.
// Statistic data returned as an ArrayList where each element represents the value of one day
        val hiHealthDataQuery = HiHealthDataQuery(HiHealthPointType.DATA_POINT_STEP_SUM, startTime,
                endTime, HiHealthDataQueryOption())
        HiHealthDataStore.execQuery(activity, hiHealthDataQuery, timeout) { resultCode, data ->
            Log.i(TAG, "enter query onSuccess")
            if (data != null && data is ArrayList<*>) {
                val dataList: List<HiHealthPointData> = data as List<HiHealthPointData>
                val stringBuffer = StringBuffer("")
                for (obj in dataList) {
                    stringBuffer.append(obj.type.toString())
                            .append(":")
                    stringBuffer.append(obj.value.toString())
                            .append(";")
                }
                Log.i(TAG, "resultCode is $resultCode data: $stringBuffer")
            } else {
                Log.i(TAG, "User has no data these days or cancel authorization.")
            }
        }
    }
}