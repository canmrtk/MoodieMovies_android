plugins {
    alias(libs.plugins.android.application)
    // 🔹 Gerekli plugin eklendi
}

android {
    namespace = "com.moodiemovies"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moodiemovies"
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
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.google.android.gms:play-services-auth:21.2.0") // 🔹 Google Sign-In desteği

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")

    implementation("androidx.recyclerview:recyclerview:1.3.1")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
