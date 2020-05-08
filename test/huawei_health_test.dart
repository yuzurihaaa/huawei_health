import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:huawei_health/huawei_health.dart';

void main() {
  const MethodChannel channel = MethodChannel('huawei_health');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await HuaweiHealth.authorizeHuawei, '42');
  });
}
