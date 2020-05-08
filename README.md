# huawei_health

A new Flutter project.

## Prerequisite

1. Register yourself or your company at https://developer.huawei.com/.
2. Register your app at Console > Smart Living > HiHealth Kit

## Getting Started

**Running example**
1. Create a copy of `example.properties` from [example.properties.bak](example/android/example.properties.bak)
2. Replace your id and huawei id in [example.properties](example/android/example.properties)
3. Run the project.

**Using in your own project**
1. Add this into your `AndroidManifest.xml`
```xml
<meta-data
            android:name="com.huawei.hms.client.appid"
            android:value="${huaweiID}"/>
```
2. Add to `pubspec.yaml`

> // TODO: Adding to pubspec.yaml example.

3. Import
```dart
import 'package:huawei_health/huawei_health.dart';
```

## Usage.

// TODO: Add Example.
