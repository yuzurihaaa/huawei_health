package com.huawei.hms.huawei_health.scenes

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull
import com.huawei.hihealthkit.HiHealthDataQuery
import com.huawei.hihealthkit.HiHealthDataQueryOption
import com.huawei.hihealthkit.auth.HiHealthAuth
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType
import com.huawei.hihealthkit.data.HiHealthPointData
import com.huawei.hihealthkit.data.store.HiHealthDataStore
import com.huawei.hihealthkit.data.type.HiHealthPointType
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "HmsLoginScene"
private const val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSS"
private const val TIMEOUT = 0

class HmsMethodHandler : MethodChannel.MethodCallHandler {

    private var activity: Activity? = null

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        when (call.method) {
            "loginHuawei" -> {
                hmsLogin(result)
            }
            "getSteps" -> {
                getSteps(call, result)
            }
            "getDistance" -> {
                getDistance(call, result)
            }
            else -> result.notImplemented()
        }
    }

    private fun hmsLogin(result: MethodChannel.Result) {
        val read = intArrayOf(
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_STEP_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_DISTANCE_SUM)
        val write = intArrayOf()

        HiHealthAuth.requestAuthorization(activity, write, read) { code, reason ->
            Log.i(TAG, "requestAuthorization onResult: $code")
            Log.i(TAG, "requestAuthorization onResult: $reason")
            activity?.runOnUiThread {
                if (code != 0) {
                    result.error("$code","Fail to authorize user", "$reason")
                } else {
                    result.success("Opening authorization screen")
                }
            }

        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSteps(call: MethodCall, result: MethodChannel.Result) {
        val (startTime, endTime) = getTime(call)

        val hiHealthDataQuery = HiHealthDataQuery(HiHealthPointType.DATA_POINT_STEP_SUM, startTime,
                endTime, HiHealthDataQueryOption())

        HiHealthDataStore.execQuery(activity, hiHealthDataQuery, TIMEOUT) { resultCode, data ->
            Log.i(TAG, "result code $resultCode")
            if (data != null && data is ArrayList<*>) {
                val output = setToMap(data = data as List<HiHealthPointData>)
                activity?.runOnUiThread {
                    result.success(output)
                }
            } else {
                activity?.runOnUiThread {
                    result.error(resultCode.toString(), "Data is null", null)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getDistance(call: MethodCall, result: MethodChannel.Result) {
        val (startTime, endTime) = getTime(call)

        val hiHealthDataQuery = HiHealthDataQuery(HiHealthPointType.DATA_POINT_DISTANCE_SUM, startTime,
                endTime, HiHealthDataQueryOption())

        HiHealthDataStore.execQuery(activity, hiHealthDataQuery, TIMEOUT) { resultCode, data ->
            Log.i(TAG, "result code $resultCode")
            if (data != null && data is ArrayList<*>) {
                val output = setToMap(data = data as List<HiHealthPointData>)
                activity?.runOnUiThread {
                    result.success(output)
                }
            } else {
                activity?.runOnUiThread {
                    result.error(resultCode.toString(), "Data is null", null)
                }
            }
        }
    }

    private fun getTime(call: MethodCall): Pair<Long, Long> {

        Log.i(TAG, "result code ${call.arguments}")
        val endTime = call.argument<String>("endTime")
        val endTimeInLong = SimpleDateFormat(ISO_FORMAT, Locale.US).parse(endTime)

        val startTime = call.argument<String>("startTime")
        val startTimeInLong = SimpleDateFormat(ISO_FORMAT, Locale.US).parse(startTime)

        return Pair(startTimeInLong.time, endTimeInLong.time)
    }

    private fun setToMap(data: List<HiHealthPointData>): Map<String, Int> {
        val dataMap = mutableMapOf<String, Int>()
        val dataList: List<HiHealthPointData> = data
        for (obj in dataList) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = obj.startTime

            val dateFormat = SimpleDateFormat(ISO_FORMAT, Locale.US)
            dataMap[dateFormat.format(calendar.time)] = obj.value
        }
        return dataMap
    }
}