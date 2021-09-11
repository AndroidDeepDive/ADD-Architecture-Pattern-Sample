buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.Kotlin.VERSION}")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.Hilt.VERSION}")
    }
}
