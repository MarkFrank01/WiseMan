# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes InnerClasses
-keep class * extends com.zxcx.zhizhe.retrofit.RetrofitBean { *; }
-keep class com.zxcx.zhizhe.retrofit.** {*;}
#Okhttp+Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.annotation.**
    # Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
    # Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
    # Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
    # Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
    # FastJson
-dontwarn com.alibaba.fastjson.**
        #-keep class com.alibaba.fastjson.** { *; }
-keepattributes *Annotation*
#EventBus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
        # Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
   *;
}
#Butterknife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
-dontwarn com.bumptech.glide.load.resource.bitmap.Downsampler
-dontwarn com.bumptech.glide.load.resource.bitmap.HardwareConfigState
#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# SMSSDK
-keep class com.mob.**{*;}
-keep class cn.smssdk.**{*;}
-dontwarn com.mob.**
#ShareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-keep class m.framework.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**
-dontwarn **.R$*
#WeChat
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
#OSS
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**
#保持Behavior类名不变
-keep public class * extends android.support.design.widget.CoordinatorLayout.Behavior { *; }
#JPush
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#JAnalytics
-keep public class cn.jiguang.analytics.android.api.** { *; }
#LoadSir
-dontwarn com.kingja.loadsir.**
-keep class com.kingja.loadsir.** {*;}
#Kotlin
-dontwarn kotlin.reflect.jvm.internal.**
#JS
-keepattributes *JavascriptInterface*
-keep class com.zxcx.zhizhe.**{*;}
-keep class com.zxcx.zhizhe.utils.**{*;}

########################################
#待处理
-keep class com.uuch.adlibrary.**

########################################
-optimizationpasses 5
-dontpreverify
-verbose
-ignorewarning
