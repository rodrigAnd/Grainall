plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.fiap.grainall"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fiap.grainall"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    // implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    testImplementation("junit:junit:4.13.2")
    //  androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Lottie
    implementation("com.airbnb.android:lottie:6.3.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries


    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-perf")
    implementation("com.google.firebase:firebase-storage-ktx")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("io.insert-koin:koin-android:3.2.0")


  //  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")




   // implementation("io.insert-koin:koin-core-coroutines:3.5.3")

   // implementation("io.insert-koin:koin-bom:3.5.3")
  //implementation("io.insert-koin:koin-android-viewmodel:2.2.3")
    //implementation("org.koin:koin-androidx-viewmodel:2.2.1")
    // Java Compatibility
    //implementation ("io.insert-koin:koin-android-compat:3.5.3")
   // implementation("io.insert-koin:koin-android-viewmodel")
  //  implementation ("io.insert-koin:koin-android-compat")
    // Jetpack WorkManager
    //  implementation ("io.insert-koin:koin-androidx-workmanager:3.5.3")
    // Navigation Graph
    // implementation ("io.insert-koin:koin-androidx-navigation:3.5.3")
   // implementation ("io.coil-kt:coil:2.5.0")
    implementation ("io.coil-kt:coil:2.5.0")
    implementation ("com.squareup.picasso:picasso:2.8")
}
kapt {
    correctErrorTypes = true
}
