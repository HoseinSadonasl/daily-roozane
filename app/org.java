# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/{user}/android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
#
#-dontoptimize
#-verbose
#
#-repackageclasses ''
#-allowaccessmodification
#-optimizations !code/simplification/arithmetic
#
#-keepattributes *Annotation*,Signature,EnclosingMethod,ElementList,Root,Annotation,InnerClasses
#
##Android
#
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * implements java.io.Serializable
#-keep public class * extends android.os.AsyncTask
#
#-keep class com.abc.daily.domain.use_case.*
#-keep class com.abc.daily.domain.model.*
#
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.content.Context {
#   public void *(android.view.View);
#   public void *(android.view.MenuItem);
#}
#
#-keepclassmembers class * implements android.os.Parcelable {
#    static ** CREATOR;
#}
#
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
##Apache
#
#-dontwarn org.apache.lang.**
#-keep class org.apache.http.** { *; }
#-keep class org.apache.james.mime4j.** { *; }
#
##Amazon
#
#-keep class com.amazonaws.javax.xml.transform.sax.*     { public *; }
#-keep class com.amazonaws.javax.xml.stream.**           { *; }
#-keep class com.amazonaws.services.**.model.*Exception* { *; }
#-keep class com.amazonaws.internal.** 					{ *; }
#-keep class org.codehaus.**                             { *; }
#-keep class org.joda.convert.*							{ *; }
#-keepnames class com.fasterxml.jackson.** { *; }
#-keepnames class com.amazonaws.** { *; }
#
#-dontwarn org.joda.time.**
#-dontwarn com.fasterxml.jackson.databind.**
#-dontwarn javax.xml.stream.events.**
#-dontwarn org.codehaus.jackson.**
#-dontwarn org.apache.commons.logging.impl.**
#-dontwarn org.apache.http.conn.scheme.**
#-dontwarn org.apache.http.annotation.**
#-dontwarn org.ietf.jgss.**
#-dontwarn org.w3c.dom.bootstrap.**
#
#-keep class android.** { *; }
#-keep interface android.** { *; }
#-keep class org.ietf.** { *; }
#-keep interface org.ietf.** { *; }
#
#-dontwarn com.amazon.**
#-dontwarn com.amazonaws.**
#
#-keep class com.amazon.** { *; }
#-dontwarn org.apache.http.annotation.**
#-keep class com.amazonaws.** { *; }
#-keep class org.apache.commons.logging.**               { *; }
#-keep class com.amazonaws.javax.xml.transform.sax.*     { public *; }
#-keep class com.amazonaws.javax.xml.stream.**           { *; }
#-keep class com.amazonaws.services.**.model.*Exception* { *; }
#-keep class org.codehaus.**                             { *; }
#
#-dontwarn javax.xml.stream.events.**
#-dontwarn org.codehaus.jackson.**
#-dontwarn org.apache.commons.logging.impl.**
#-dontwarn org.apache.http.conn.scheme.**
#
#-keep class android.webkit.** { *; }
#
##Retrofit
#
#-dontwarn retrofit.**
#-dontwarn rx.**
#-dontwarn okio.**
#-dontwarn com.squareup.okhttp.*
#-keep class retrofit.** { *; }
#-keep class com.google.gson.** { *; }
#-keep class com.google.inject.** { *; }
#-keep class javax.inject.** { *; }
#-keep class retrofit.** { *; }
#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}
#
## Retrofit, OkHttp, Gson
#
#-keep class com.squareup.okhttp.** { *; }
#-keep interface com.squareup.okhttp.** { *; }
#-dontwarn com.squareup.okhttp.**
#-dontwarn java.nio.file.*
#
#
#
##RxJava
#
#-keep class com.google.appengine.** { *; }
#
##SimpleFramework.xml
#-dontwarn org.simpleframework.xml.stream.**
#-keep public class org.simpleframework.** { *; }
#-keep class org.simpleframework.xml.** { *; }
#-keep class org.simpleframework.xml.core.** { *; }
#-keep class org.simpleframework.xml.util.** { *; }
#-keep class javax.xml.stream.** { *; }
#
#-keepclassmembers class * {
#    @org.simpleframework.xml.* *;
#}
#
##Instructure
#
#-keep public class com.instructure.** { *; }
#
##Fix for Samsung Light - AppCompat issue
#
#-keep class android.support.v4.app.** { *; }
#-keep interface android.support.v4.app.** { *; }
#
#-keep class android.support.v7.app.** { *; }
#-keep interface android.support.v7.app.** { *; }
#
#-keep class android.support.v13.app.** { *; }
#-keep interface android.support.v13.app.** { *; }
#
#-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}
#
## Add any project specific keep options here:
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
##-keepclassmembers class fqcn.of.javascriptToInject.interface.for.webview {
##   public *;
##}
#
#-dontwarn okio.**
#-dontwarn okhttp3.**
#-dontwarn retrofit2.**
#
#
### keep Enum in Response Objects
#-keepclassmembers enum com.android.services.** { *; }
#
#
### Note not be needed unless some model classes don't implement Serializable interface
### Keep model classes used by ORMlite
#-keep class com.android.model.**
#
#
### keep classes and class members that implement java.io.Serializable from being removed or renamed
### Fixes "Class class com.twinpeek.android.model.User does not have an id field" execption
#-keep class * implements java.io.Serializable {
#    *;
#}
##
#### Rules for Retrofit2
#-keepclasseswithmembers class * {
#    @retrofit2.http.* <methods>;
#}
# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keepclassmembers,allowobfuscation class * {
 @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers class com.abc.daily.domain.model.weather.Weather {
 !transient <fields>;
}
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep class kotlin.coroutines.Continuation
-keepclassmembers enum * { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.dubai.fa.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
#Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
mikha ychikar koni?ye proguard jadid dorods koanm nmikhad hamin chshe