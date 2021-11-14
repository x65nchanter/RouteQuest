import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":core"))

    // Android Compose
    implementation(libs.bundles.androidKtx)
    implementation(libs.bundles.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidTest)
    debugImplementation(libs.uiTooling)

    // Dagger Hilt
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    androidTestImplementation(libs.hiltAndroidTesting)
    kaptAndroidTest(libs.hiltCompiler)

    testImplementation(libs.hiltAndroidTesting)
    kaptTest(libs.hiltCompiler)

    // AirBNB Mavericks
    implementation(libs.bundles.mavericks)

    // Network
    implementation(libs.flowreactivenetwork)
    implementation(libs.coilCompose)
    implementation(libs.bundles.retrofit)
    implementation(libs.okhttp)

    // Map
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
    implementation("com.google.maps.android:maps-v3-ktx:2.2.0")
    implementation("androidx.fragment:fragment:1.3.6")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.cocoahero.android:geojson:1.0.1@jar")
}

android {
    compileSdk = 31

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }

    defaultConfig {
        applicationId = "com.example.routequest"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}
