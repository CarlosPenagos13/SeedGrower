buildscript {
    val kotlinVersion = "1.6.21"

    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath(kotlin("gradle-plugin", version = "$kotlinVersion"))
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("com.jakewharton:butterknife-gradle-plugin:10.2.3")
    }
}

plugins {
    val kotlinVersion = "1.9.10"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    id("com.android.application") version "8.1.1" apply false
    alias(libs.plugins.kotlinAndroid) apply false

}