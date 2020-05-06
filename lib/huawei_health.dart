import 'dart:async';

import 'package:flutter/services.dart';

class HuaweiHealth {
  static const MethodChannel _channel =
      const MethodChannel('huawei_health');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('loginHuawei');
    return version;
  }
}
