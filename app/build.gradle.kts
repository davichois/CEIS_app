plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safe.args)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.davichois.ceis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.davichois.ceis"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Dagger Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)


    // Navigation
    implementation(libs.jetpack.compose.integration)
    implementation(libs.fragments.integration)
    implementation(libs.views.integration)
    implementation(libs.feature.module.support.fragments)
    implementation(libs.testing.navigation)

    // Responsive dimensions
    implementation(libs.responsive.dimensions.sdp)
    implementation(libs.responsive.dimensions.ssp)

    // zxing
    implementation(libs.zxing)

    // extra zxing
    implementation(libs.zxing.android.embedded)
    implementation(libs.zxing.legacy.support)

    // Shimmer
    implementation(libs.shimmer)

    // Scan QR
    implementation(libs.code.scanner)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)

    // JWT
    implementation(libs.jwtDecode)

    // Splash Screen
    implementation(libs.coreSplashscreen)

    // Picasso
    implementation(libs.picasso)

    // Rive
    implementation(libs.rive.android)
    implementation(libs.startup.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}