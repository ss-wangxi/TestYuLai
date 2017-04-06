# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\AndroidEnv\android-sdk-windows/tools/proguard/proguard-android.txt
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


# Ximalaya Begin
-dontwarn okio.**
-keep class okio.** { *;}

-dontwarn okhttp3.**
-keep class okhttp3.** { *;}

-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

-dontwarn android.support.**
-keep class android.support.** { *;}

-dontwarn com.ximalaya.ting.android.player.**
-keep class com.ximalaya.ting.android.player.** { *;}

-dontwarn com.ximalaya.ting.android.opensdk.**
-keep interface com.ximalaya.ting.android.opensdk.** {*;}
-keep class com.ximalaya.ting.android.opensdk.** { *; }
# Ximalaya End

