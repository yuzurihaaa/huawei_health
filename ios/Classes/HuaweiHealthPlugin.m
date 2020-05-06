#import "HuaweiHealthPlugin.h"
#if __has_include(<huawei_health/huawei_health-Swift.h>)
#import <huawei_health/huawei_health-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "huawei_health-Swift.h"
#endif

@implementation HuaweiHealthPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftHuaweiHealthPlugin registerWithRegistrar:registrar];
}
@end
