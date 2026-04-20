# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---- Firestore model classes (used with toObject / add) ----
# Keep all fields so Firestore reflection can match document keys
-keep class com.example.mychattingapp.LocaldbLogics.DAO.Entities.** { *; }

# ---- Retrofit + Gson ----
# Keep Retrofit API interfaces
-keep,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Keep GitHub API models used with Gson
-keep class com.example.mychattingapp.ProfilePicUploadLogics.** { *; }

# Gson uses generic type information at runtime
-keepattributes Signature
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**