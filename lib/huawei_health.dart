import 'dart:async';

import 'package:flutter/services.dart';
import 'package:url_launcher/url_launcher.dart';

class HuaweiHealth {
  static const MethodChannel _channel =
      const MethodChannel('huawei_health');

  static Future<String> get authorizeHuawei async {
    final String version = await _channel.invokeMethod('loginHuawei');
    return version;
  }

  /// TODO: Verify with Huawei team how to check if the app is installed.
  static Future<bool> isHuaweiHealthAppInstalled() async {
    if (await canLaunch("huawei://")) {
      print('has app');
      return true;
    } else {
      print('has no app');
      return false;
    }
  }

  static Future getSteps() async {
    final steps = await _channel.invokeMethod('getSteps');
  }
}
