import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// SECURITY: the AI chat API key is read from local.properties, which is listed
// in .gitignore and is therefore NEVER committed to GitHub.
// If the key is missing or empty, the app automatically runs in demo mode
// (offline mock answers), so the app works without any API key.
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}
val cyberAiApiKey: String = localProperties.getProperty("CYBER_AI_API_KEY") ?: ""

android {
    namespace = "com.example.cyberaigudie"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.cyberaigudie"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // To connect a real AI later, add this line to local.properties:
        // CYBER_AI_API_KEY=your_key_here
        buildConfigField("String", "CYBER_AI_API_KEY", "\"$cyberAiApiKey\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
