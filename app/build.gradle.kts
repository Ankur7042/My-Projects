plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.whatsappclone_chatapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.whatsappclone_chatapplication"
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

    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database")
    implementation ("com.github.bumptech.glide:glide:4.11.0")

    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.gms:play-services-safetynet:18.0.1")

    val lifecycle_version = "2.0.0"
    // ViewModel and LiveData
    implementation ("androidx.lifecycle:lifecycle-extensions:$lifecycle_version")
// alternatively - just ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version") // For Kotlin use lifecycle-viewmodel-ktx
// alternatively - just LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")


}