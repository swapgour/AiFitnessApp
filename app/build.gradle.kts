import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")

    // ⭐ REQUIRED for Kotlin 2.0+ (Compose Compiler Plugin)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.aifitnessapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.aifitnessapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // --------------------------------------------------
        // ✅ Load API Keys from local.properties
        // --------------------------------------------------
        val localProps = Properties()
        val propsFile = rootProject.file("local.properties")
        if (propsFile.exists()) {
            propsFile.inputStream().use { localProps.load(it) }
        }

        val groqKey = localProps.getProperty("GROQ_API_KEY") ?: ""
        val openaiKey = localProps.getProperty("OPENAI_API_KEY") ?: ""
        val geminiKey = localProps.getProperty("GEMINI_API_KEY") ?: ""

        buildConfigField("String", "GROQ_API_KEY", "\"$groqKey\"")
        buildConfigField("String", "OPENAI_API_KEY", "\"$openaiKey\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // --------------------------------------------------
    // Compose + BuildConfig
    // --------------------------------------------------
    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Keep same keys in debug too (already included in defaultConfig)
            isDebuggable = true
        }
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

    // Core Android libraries
    implementation(libs.androidx.core.ktx)

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ⭐ Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // Compose UI toolkit
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)

    // Debug tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --------------------------------------------------
    // ROOM Database
    // --------------------------------------------------
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // OkHttp (used in AI Coach + DietPlan APIs)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Retrofit (if used later)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")
}
