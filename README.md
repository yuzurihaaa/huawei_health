# HiHealth 

### [Disclaimer] This is not an official huawei hihealth plugins.
* You must head to https://developer.huawei.com/consumer/en/ and create your account and app account.
* This plugin is a wrapper for their `.jar` and android implementation.
* Head to this [link](https://developer.huawei.com/consumer/en/doc/development/health-Library/31206) if you prefer to implement it yourself.
* HiHealth is only available on Android. For iOS, use Apple Health.

## Prerequisite

1. Register yourself or your company at https://developer.huawei.com/.
2. Register your app at Console > Smart Living > HiHealth Kit
3. You may need to tweak [gradle](example/android/app/build.gradle) depends if you are using
your own keystore. Keystore is ignored. 

## Getting Started

**Running example**
1. Create a copy of `example.properties` from [example.properties.bak](example/android/example.properties.bak)
2. Replace your data in [example.properties](example/android/example.properties)
3. Run the project.

**Using in your own project**
1. Add this into your `AndroidManifest.xml`
```xml
<meta-data
            android:name="com.huawei.hms.client.appid"
            android:value="${huaweiID}"/>
```
2. Add to `pubspec.yaml`

```yaml
huawei_health: any 
```


3. Import
```dart
import 'package:hi_health/hi_health.dart';
```

## Usage.
To get the Steps data:
```dart
Future getSteps() async {
    final start = DateTime.now().subtract(Duration(days: 5));
    final end = DateTime.now();
    final steps = await HiHealth.getSteps(start, end);
}
```

To get the distance data:
```dart
Future getDistances() async {
    final start = DateTime.now().subtract(Duration(days: 5));
    final end = DateTime.now();
    final distances = await HiHealth.getDistance(start, end);
}
```

## Release
If you enable minify, add this line into your proguard rules. This will avoid exception when unmarshalling class.
```proguard
-keep class com.huawei.hihealth.* {
   public *;
}
```
