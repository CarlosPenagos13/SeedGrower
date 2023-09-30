plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.jakewharton.butterknife")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.seedgrower"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.seedgrower"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures{
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //socket.io
    implementation("io.socket:socket.io-client:2.0.0")

    // ButterKnife
    implementation("com.jakewharton:butterknife:10.2.1")
    annotationProcessor("com.jakewharton:butterknife-compiler:10.2.3")
    kapt("com.jakewharton:butterknife-compiler:10.2.3")

    //GSON
    val gsonVersion = "2.8.5"
    api("com.google.code.gson:gson:$gsonVersion")

    //PREFS
    implementation("com.pixplicity.easyprefs:EasyPrefs:1.10.0")

    //GEOLOCATION
    // https://mvnrepository.com/artifact/com.google.android.gms/play-services-location
    implementation("com.google.android.gms:play-services-location:21.0.1")


}