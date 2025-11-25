
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.viewmodela"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.viewmodela"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}





dependencies {
    // --- LIBRERÍAS NUEVAS Y ESENCIALES PARA EL DISEÑO ---
    // Foundation: Proporciona los bloques de construcción básicos de UI como LazyColumn, Row, Box, y el nuevo Pager.
    implementation(libs.androidx.compose.foundation)
    // Íconos de Material Design (Icons.Default.Build, etc.)
    implementation(libs.androidx.compose.material.icons.extended)

    // --- LIBRERÍAS QUE YA TENÍAS ---
    // Navegación en Compose
    implementation(libs.androidx.navigation.compose)

    // ViewModel para Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose) // Ya no suele ser necesaria si usas el BOM

    // Coroutines de Kotlin para Android
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android)

    // Core KTX y Activity Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose Bill of Materials (BOM) - Gestiona las versiones de Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit for network operations
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")
}

