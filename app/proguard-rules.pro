-optimizationpasses 5 # 指定代码的压缩级别
-dontusemixedcaseclassnames # 是否使用大小写混合
-dontpreverify # 混淆时是否做预校验
-verbose # 混淆时是否记录日志
-ignorewarnings # 忽略警告
-dontskipnonpubliclibraryclasses
-keepattributes SourceFile,LineNumberTable

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* # 混淆时所采用的算法
-keep public class * extends android.app.Activity # 保持哪些类不被混淆
-keep public class * extends android.app.Application # 保持哪些类不被混淆
-keep public class * extends android.app.Service # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService # 保持哪些类不被混淆


-keepclasseswithmembernames class * { # 保持 native 方法不被混淆
native <methods>;
}
-keepclasseswithmembers class * { # 保持自定义控件类不被混淆
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
public void *(android.view.View);
}
-keepclassmembers enum * { # 保持枚举 enum 类不被混淆
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable { #保持 Serializable 不被混淆
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}



##---------------自身 start  ----------

-keep class com.weiyankeji.zhongmei.ui.mmodel.**{*;}

-keep public class * extends com.weiyankeji.zhongmei.ui.fragment.BaseListFragment
-keep public class * extends com.weiyankeji.zhongmei.ui.fragment.BaseMvpFragment

# wy-android-library
-keep class com.weiyankeji.library.customview.**{*;}
-keep class com.weiyankeji.library.utils.**{*;}
-keep class com.weiyankeji.library.net.**{*;}

##---------------自身 end  ----------




##---------------第三方 start  ----------

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#glide
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

##广告轮播
-keep class com.bigkoo.convenientbanner.**{*;}

#个推
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

#微信
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
#MAT
-keep class com.tencent.stat.** { *;}
-keep class com.tencent.mid.** { *;}
##Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
##eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


##---------------第三方 end  ----------



