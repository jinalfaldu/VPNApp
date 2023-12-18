



-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**
-dontwarn com.google.errorprone.**
-dontwarn sun.misc.Unsafe
-dontwarn java.lang.ClassValue

-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue
-dontwarn com.google.common.util.concurrent.FuturesGetChecked**
-dontwarn javax.lang.model.element.Modifier
-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
-keep class com.facebook.ads.* { *; }
-dontwarn com.facebook.ads.**

-flattenpackagehierarchy




-keepnames class com.google.android.gms.** {*;}

-keep class com.fasterxml.** { *;}

-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

-keep class br.project.pine.** { *;}
-keep class com.google.api.services.drive.** { *;}



-ignorewarnings
-dontwarn io.reactivex.functions.Function.**
-dontwarn io.reactivex.functions.Action.**
-dontwarn io.reactivex.functions.CompletableOnSubscribe.**
-dontwarn io.reactivex.functions.Consumer.**

-keep class com.squareup.okhttp.**{*;}
-keep class com.squareup.okhttp3.** {*;}

-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.* { *; }
-keep interface okhttp3.* { *; }


-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

-keepattributes Signature

-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-dontwarn okio.**
-keep class retrofit2.** { *; }

-keep class sun.misc.Unsafe { *; }

-keep class org.simpleframework.xml.** { *; }

# SimpleXML
-keep interface org.simpleframework.xml.core.Label {
   public *;
}
-keep class * implements org.simpleframework.xml.core.Label {
   public *;
}
-keep interface org.simpleframework.xml.core.Parameter {
   public *;
}
-keep class * implements org.simpleframework.xml.core.Parameter {
   public *;
}
-keep interface org.simpleframework.xml.core.Extractor {
   public *;
}
-keep class * implements org.simpleframework.xml.core.Extractor {
   public *;
}

-keep class org.simpleframework.xml.Element** { *; }
-keep class org.simpleframework.xml.Root** { *; }




-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
-keep class org.xmlpull.v1.* {*;}

-flattenpackagehierarchy

-adaptresourcefilenames
-adaptresourcefilecontents
-verbose


-dontwarn InnerClasses



