plugins {
    alias(libs.plugins.androidApplication)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pillwatch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pillwatch"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.ext.junit)
    testImplementation(libs.espresso.intents)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-database")

    implementation("com.google.firebase:firebase-firestore")

    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")

    // Add the dependency for testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.rules)
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")


    // Mockito for mocking in unit tests
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:5.11.0")

    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation ("org.mockito:mockito-junit-jupiter:5.11.0")

    testImplementation ("org.robolectric:robolectric:4.12.1")

    // AndroidX Test dependencies for instrumented tests
    androidTestImplementation("androidx.test:runner:1.4.0")

}