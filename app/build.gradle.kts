plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.leanmass"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.leanmass"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Firebase BOM (manages all Firebase versions)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firestore (replaces SQLite for user data)
    implementation("com.google.firebase:firebase-firestore-ktx")

}