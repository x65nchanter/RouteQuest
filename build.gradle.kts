plugins {
    kotlin("android") apply false
    kotlin("jvm") apply false
    kotlin("kapt") apply false
    id("com.android.application") apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.0" apply false
    id("dagger.hilt.android.plugin") version "2.39.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
