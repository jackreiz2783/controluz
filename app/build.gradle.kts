plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.controluz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.controluz"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.appcompat)  // Dependencia de appcompat
    implementation(libs.material)  // Dependencia de material

    // Ahora agregas la dependencia de OkHttp
    implementation(libs.okhttp)  // Aquí hace referencia a lo que configuraste en libs.versions.toml

    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}