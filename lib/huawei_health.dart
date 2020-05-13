import 'dart:async';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class HuaweiException implements Exception {
  String cause;

  HuaweiException(this.cause);
}

@immutable
class HuaweiDataClass {
  final int data;
  final String date;

  HuaweiDataClass({this.data, this.date});
}

class HuaweiHealth {
  static const MethodChannel _channel = const MethodChannel('huawei_health');

  static Future authorizeHuawei() {
    if (!Platform.isAndroid) {
      throw HuaweiException('Huawei HiHealth only available on android. Refer '
          'https://developer.huawei.com/consumer/en/doc/development/health-Library/31206'
          'for more info');
    }
    return _channel.invokeMethod('loginHuawei');
  }

  /// [startTime] is the time start (previous time)
  /// [endTime] is the latest date when we want to query.
  static Future<List<HuaweiDataClass>> getSteps(
      DateTime startTime, DateTime endTime) async {
    assert(startTime != null);
    assert(endTime != null);
    assert(endTime.compareTo(startTime) >= 0);
    try {
      return await _getData('getSteps', startTime, endTime);
    } on PlatformException catch (_) {
      // There are times where the 2nd call works.
      try {
        return await _getData('getSteps', startTime, endTime);
      } on PlatformException catch (_) {
        throw HuaweiException("Unable to get Steps");
      }
    }
  }

  /// [startTime] is the time start (previous time)
  /// [endTime] is the latest date when we want to query.
  static Future<List<HuaweiDataClass>> getDistance(
      DateTime startTime, DateTime endTime) async {
    if (!Platform.isAndroid) {
      throw HuaweiException('Huawei HiHealth only available on android. Refer '
          'https://developer.huawei.com/consumer/en/doc/development/health-Library/31206'
          'for more info');
    }

    assert(startTime != null);
    assert(endTime != null);
    assert(endTime.compareTo(startTime) >= 0);

    try {
      return await _getData('getDistance', startTime, endTime);
    } on PlatformException catch (_) {
      // There are times where the 2nd call works.
      try {
        return await _getData('getDistance', startTime, endTime);
      } on PlatformException catch (_) {
        throw HuaweiException("Unable to get Distance");
      }
    }
  }

  static Future<List<HuaweiDataClass>> _getData(
      String method, DateTime startTime, DateTime endTime) async {
    if (!Platform.isAndroid) {
      throw HuaweiException('Huawei HiHealth only available on android. Refer '
          'https://developer.huawei.com/consumer/en/doc/development/health-Library/31206'
          'for more info');
    }

    final startTimeInString = startTime.toIso8601String();
    final endTimeInString = endTime.toIso8601String();
    var data = <HuaweiDataClass>[];
    final steps = await _channel.invokeMethod(method, <String, String>{
      'startTime': startTimeInString,
      'endTime': endTimeInString,
    });

    if (steps != null && steps.isNotEmpty) {
      steps.keys.forEach((date) {
        data.add(HuaweiDataClass(data: steps[date], date: date));
      });
    }

    return data;
  }
}
