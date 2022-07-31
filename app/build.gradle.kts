plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = 32
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "app.bluetooth.mesh"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(project(":utilities"))
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.7.0")
    /** material design **/
    implementation("androidx.activity:activity-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    /** nav component **/
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.0")

    /** hilt dagger **/
    implementation("com.google.dagger:hilt-android:2.40.5")

    kapt("com.google.dagger:hilt-compiler:2.41")

    /** view model **/
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.0-alpha01")

    /** lifecycle **/
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")

    /** lifecycle optional **/
    implementation("androidx.lifecycle:lifecycle-service:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-process:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.6.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha01")

    /** reactive stream coroutines **/
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    /** mesh network **/
    implementation("live.ditto:ditto:2.0.0-alpha1")

    /** logging utilities **/
    implementation("com.jakewharton.timber:timber:5.0.1")

    testImplementation(project(mapOf("path" to ":domain")))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
