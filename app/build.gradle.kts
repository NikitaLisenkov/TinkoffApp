plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
        animationsDisabled = true
    }
}

dependencies {
    ksp(libs.daggerCompiler)
    ksp(libs.roomCompiler)

    implementation(libs.viewBindingDelegate)
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.okhttp)
    implementation(libs.loggingInterceptor)
    implementation(libs.coreKtx)
    implementation(libs.fragmentKtx)
    implementation(libs.activityKtx)
    implementation(libs.viewModelKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintLayout)
    implementation(libs.swipeRefreshLayout)
    implementation(libs.dagger)
    implementation(libs.shimmer)
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)

    testImplementation(libs.testRules)
    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    testImplementation(libs.kotestRunner)
    testImplementation(libs.kotestAssertions)
    testImplementation(libs.kotestProperty)
    androidTestImplementation(libs.hamcrest)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.espressoIntents)
    debugImplementation(libs.testCore)
    androidTestImplementation(libs.httpClientAndroid)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
}