plugins {
    id("com.android.application")
    kotlin("android")
    id("com.apollographql.apollo3").version("3.8.2")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    id("androidx.navigation.safeargs.kotlin")

}

kapt {
    correctErrorTypes = true
}


apollo {

    service("service") {

        packageName.set("com.immersion")

    }

}

android {
    namespace = "com.immersion.immersionandroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.immersion.immersionandroid"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // implementation("androidx.core:core-ktx:1.10.1")
    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.fragment:fragment-ktx:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("io.github.sceneview:arsceneview:0.9.10")
    implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.46.1")
    // implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    // kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}