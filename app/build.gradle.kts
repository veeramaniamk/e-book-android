plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.saveetha.e_book"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.saveetha.e_book"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:3.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")

    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

//    load gif file
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.1.0")

//    glide for load image url
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    //for razorpay
    implementation("com.razorpay:checkout:1.6.36")
    implementation("com.joanzapata.pdfview:android-pdfview:1.0.4@aar")
    implementation("com.google.android.play:app-update:2.1.0")

    //load pdf url
//    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    //for external pdf viewer  library
//    implementation("com.github.barteksc:pdfium-android:1.7.1")
//    implementation("om.github.barteksc:android-pdf-viewer:2.8.2")
//    implementation("com.github.barteksc:android-pdf-viewer:3.2.0")
}