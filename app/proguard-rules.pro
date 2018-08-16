# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/yangjing/Android/Sdk/tools/proguard/proguard-android.txt
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

-ignorewarnings

-keep class com.jkb.fragment.rigger.rigger.** {*;}
-keep interface com.jkb.fragment.rigger.rigger.** {*;}
-keep class com.jkb.fragment.swiper.**{*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keepclassmembers class * extends android.app.Activity {
   public int getContainerViewId();
   public void onRiggerBackPressed();
   public void onFragmentResult(int,int,android.os.Bundle);
   public void onLazyLoadViewCreated(android.os.Bundle);
   public int[] getPuppetAnimations();
   public String getFragmentTag();
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
   public int getContainerViewId();
   public boolean onRiggerBackPressed();
   public void onFragmentResult(int,int,android.os.Bundle);
   public void onLazyLoadViewCreated(android.os.Bundle);
   public int[] getPuppetAnimations();
   public String getFragmentTag();
   public boolean onInterruptBackPressed();
}
