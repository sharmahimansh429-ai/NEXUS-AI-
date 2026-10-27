# NEXUS AI — Complete Project Source Code & Documentation

## 1. Project Overview & Architecture

- **App Name:** NEXUS
- **Platform:** Android (Jetpack Compose, Kotlin DSL)
- **Theme:** Pure Black (`#000000`), Graphite & Titanium Grey, subtle cyan/blue particle highlights
- **Core Engine:** Interactive 3D Particle Wave AI Core (over 1,600 floating dots reacting to idle, voice listening, reasoning, and speech)
- **AI Backend:** Official Google Gemini API (`gemini-3.5-flash`) via Retrofit 2 & Moshi
- **Local Persistence:** Room Database for chat messages, multi-session history, and sentient-style personal memories
- **Voice System:** On-device Speech Recognizer (STT) and Text-to-Speech (TTS) with bilingual English & Hindi support
- **Security:** Zero client-side API key leakage (Header auth, Level.NONE logging, regex sanitization, Secrets Gradle plugin)

---

## 2. Directory Structure

```
nexus/
├── metadata.json
├── .env.example
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/libs.versions.toml
└── app/
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/
        │   │   ├── MainActivity.kt
        │   │   ├── data/
        │   │   │   ├── api/
        │   │   │   │   ├── GeminiApiModels.kt
        │   │   │   │   └── GeminiApiService.kt
        │   │   │   ├── db/
        │   │   │   │   ├── ChatDao.kt
        │   │   │   │   ├── ConversationDao.kt
        │   │   │   │   ├── MemoryDao.kt
        │   │   │   │   └── NexusDatabase.kt
        │   │   │   ├── model/
        │   │   │   │   ├── ChatMessage.kt
        │   │   │   │   ├── Conversation.kt
        │   │   │   │   └── NexusMemory.kt
        │   │   │   └── repository/
        │   │   │       └── NexusRepository.kt
        │   │   ├── ui/
        │   │   │   ├── components/
        │   │   │   │   ├── CommonComponents.kt
        │   │   │   │   └── NexusOrb.kt
        │   │   │   ├── screens/
        │   │   │   │   ├── ChatScreen.kt
        │   │   │   │   ├── HistoryScreen.kt
        │   │   │   │   ├── MemoriesScreen.kt
        │   │   │   │   └── SettingsScreen.kt
        │   │   │   └── theme/
        │   │   │       ├── Color.kt
        │   │   │       ├── Theme.kt
        │   │   │       └── Type.kt
        │   │   ├── viewmodel/
        │   │   │   └── NexusViewModel.kt
        │   │   └── voice/
        │   │       ├── SpeechManager.kt
        │   │       └── TextToSpeechManager.kt
        │   └── res/values/
        │       ├── colors.xml
        │       ├── strings.xml
        │       └── themes.xml
        └── test/java/com/example/
            ├── GeminiApiIntegrationTest.kt
            ├── ExampleRobolectricTest.kt
            └── ExampleUnitTest.kt
```

---

## 3. Complete Source Code by File

### File: `metadata.json`

```json
{
  "name": "NEXUS",
  "description": "Futuristic AI Assistant with 3D animated core, bilingual Hindi & English voice, persistent memory, and local chat history.",
  "requestFramePermissions": [],
  "majorCapabilities": ["MAJOR_CAPABILITY_SERVER_SIDE_GEMINI_API"]
}

```

---

### File: `.env.example`

```properties
# GEMINI_API_KEY: Required for Gemini AI API calls.
# AI Studio automatically injects this from managed environment secrets.
# Users configure this via the Secrets panel in the AI Studio UI.
GEMINI_API_KEY=MY_GEMINI_API_KEY



```

---

### File: `build.gradle.kts`

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}

```

---

### File: `settings.gradle.kts`

```kotlin
pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "NEXUS"

include(":app")

```

---

### File: `gradle/libs.versions.toml`

```toml
[versions]
agp = "9.1.1"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.8.7"
lifecycleViewmodelCompose = "2.8.7"
lifecycleRuntimeCompose = "2.8.7"
activityCompose = "1.10.1"
kotlin = "2.2.10"
composeBom = "2024.09.00"
googleDevtoolsKsp = "2.3.5"
navigationCompose = "2.8.9"
roomRuntime = "2.7.0"
roomKtx = "2.7.0"
roomCompiler = "2.7.0"
kotlinxCoroutinesTest = "1.10.2"
core = "1.6.1"
runner = "1.6.2"
coilCompose = "2.7.0"
retrofit = "2.12.0"
converterMoshi = "2.12.0"
kotlinxCoroutinesAndroid = "1.10.2"
kotlinxCoroutinesCore = "1.10.2"
accompanistPermissions = "0.37.3"
playServicesLocation = "21.3.0"
cameraCamera2 = "1.5.0"
cameraLifecycle = "1.5.0"
cameraView = "1.5.0"
cameraCore = "1.5.0"
loggingInterceptor = "4.10.0"
okhttp = "4.10.0"
moshiKotlin = "1.15.2"
moshiKotlinCodegen = "1.15.2"
datastorePreferences = "1.1.7"
robolectric = "4.16.1"
firebaseBom = "34.17.0"
secretsGradlePlugin = "2.0.1"
googleServices = "4.5.0"
credentials = "1.5.0"
googleid = "1.1.1"


[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeCompose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutinesTest" }
androidx-core = { group = "androidx.test", name = "core", version.ref = "core" }
androidx-runner = { group = "androidx.test", name = "runner", version.ref = "runner" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coilCompose" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
converter-moshi = { group = "com.squareup.retrofit2", name = "converter-moshi", version.ref = "converterMoshi" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinxCoroutinesAndroid" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutinesCore" }
accompanist-permissions = { group = "com.google.accompanist", name = "accompanist-permissions", version.ref = "accompanistPermissions" }
play-services-location = { group = "com.google.android.gms", name = "play-services-location", version.ref = "playServicesLocation" }
androidx-camera-camera2 = { group = "androidx.camera", name = "camera-camera2", version.ref = "cameraCamera2" }
androidx-camera-lifecycle = { group = "androidx.camera", name = "camera-lifecycle", version.ref = "cameraLifecycle" }
androidx-camera-view = { group = "androidx.camera", name = "camera-view", version.ref = "cameraView" }
androidx-camera-core = { group = "androidx.camera", name = "camera-core", version.ref = "cameraCore" }
logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "loggingInterceptor" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
moshi-kotlin = { group = "com.squareup.moshi", name = "moshi-kotlin", version.ref = "moshiKotlin" }
moshi-kotlin-codegen = { group = "com.squareup.moshi", name = "moshi-kotlin-codegen", version.ref = "moshiKotlinCodegen" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastorePreferences" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-ai = { group = "com.google.firebase", name = "firebase-ai" }
firebase-appcheck-recaptcha = { group = "com.google.firebase", name = "firebase-appcheck-recaptcha" }
firebase-appcheck-debug = { group = "com.google.firebase", name = "firebase-appcheck-debug" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore" }
firebase-auth = { group = "com.google.firebase", name = "firebase-auth" }
androidx-credentials = { group = "androidx.credentials", name = "credentials", version.ref = "credentials" }
androidx-credentials-play-services = { group = "androidx.credentials", name = "credentials-play-services-auth", version.ref = "credentials" }
googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }


[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
google-devtools-ksp = { id = "com.google.devtools.ksp", version.ref = "googleDevtoolsKsp" }
secrets = { id = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin", version.ref = "secretsGradlePlugin" }
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }

```

---

### File: `app/build.gradle.kts`

```kotlin
import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.nexusai.qkmtv"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val envApiKey = System.getenv("GEMINI_API_KEY") ?: ""
    buildConfigField("String", "ENV_GEMINI_API_KEY", "\"$envApiKey\"")
  }

  // Do not remove or modify these signingConfigs. They are necessary for building, installing,
  // and updating Android apps in AI Studio.
  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      // Do not remove or modify this signingConfig assignment. It is necessary for Android apps in
      // AI Studio.
      signingConfig = signingConfigs.getByName("release")
    }
    debug {
      // Do not remove or modify this signingConfig assignment. It is necessary for Android apps in
      // AI Studio.
      signingConfig = signingConfigs.getByName("debugConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  // implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  // implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  // Uncomment to use Firestore:
  // implementation(libs.firebase.firestore)

  // Uncomment ALL FOUR of the following dependencies together to use Firebase Auth and Google
  // Sign-In via Credential Manager:
  // implementation(libs.firebase.auth)
  // implementation(libs.androidx.credentials)
  // implementation(libs.androidx.credentials.play.services)
  // implementation(libs.googleid)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.firebase.appcheck.debug)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}

```

---

### File: `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <queries>
        <intent>
            <action android:name="android.speech.RecognitionService" />
        </intent>
    </queries>

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApplication">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MyApplication">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>

```

---

### File: `app/src/main/res/values/strings.xml`

```xml
<resources>
    <string name="app_name">NEXUS</string>
</resources>

```

---

### File: `app/src/main/res/values/colors.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>

```

---

### File: `app/src/main/res/values/themes.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <style name="Theme.MyApplication" parent="android:Theme.DeviceDefault.NoActionBar" />
</resources>

```

---

### File: `app/src/main/java/com/example/ui/theme/Color.kt`

```kotlin
package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Pure Deep Black Background
val NexusPureBlack = Color(0xFF000000)
val NexusObsidian = Color(0xFF000000)
val NexusDeepBlack = Color(0xFF000000)

// Graphite & Charcoal Grey Surfaces
val NexusCharcoal = Color(0xFF090B0F)
val NexusCardDark = Color(0xFF0F1217)
val NexusSurfaceDark = Color(0xFF151921)
val NexusSurfaceElevated = Color(0xFF1E232E)

// Brushed Titanium & Metallic Silver Accents
val NexusSilverBright = Color(0xFFF1F5F9)
val NexusSilver = Color(0xFF94A3B8)
val NexusSilverMuted = Color(0xFF64748B)
val NexusSilverDark = Color(0xFF333C4D)
val NexusSilverSubtle = Color(0xFF1E2430)
val NexusBorderHairline = Color(0xFF262E3B)
val NexusTitanium = Color(0xFFCBD5E1)

// Subtle particle highlights (luminous dots)
val ParticleSilver = Color(0xFFE2E8F0)
val ParticleCyan = Color(0xFF38BDF8)
val ParticleBlue = Color(0xFF60A5FA)
val ParticleViolet = Color(0xFFA78BFA)
val ParticleTitanium = Color(0xFF94A3B8)

// Status & Indicators (refined, non-garish)
val ElectricCyan = Color(0xFFE2E8F0) // Clean metallic primary
val ElectricBlue = Color(0xFF94A3B8)
val ElectricBlueDeep = Color(0xFF475569)
val ElectricCyanGlow = Color(0x3338BDF8)
val ElectricCyanSubtle = Color(0x1A38BDF8)

val CyberViolet = Color(0xFFA78BFA)
val CyberPink = Color(0xFFF472B6)
val NeonEmerald = Color(0xFF34D399)
val NeonAmber = Color(0xFFFBBF24)
val NeonRed = Color(0xFFF87171)

```

---

### File: `app/src/main/java/com/example/ui/theme/Theme.kt`

```kotlin
package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusDarkColorScheme = darkColorScheme(
    primary = NexusSilverBright,
    onPrimary = NexusPureBlack,
    primaryContainer = NexusSurfaceElevated,
    onPrimaryContainer = NexusSilverBright,
    secondary = NexusTitanium,
    onSecondary = NexusPureBlack,
    secondaryContainer = NexusSurfaceDark,
    onSecondaryContainer = NexusSilverBright,
    tertiary = ParticleCyan,
    onTertiary = NexusPureBlack,
    background = NexusPureBlack,
    onBackground = NexusSilverBright,
    surface = NexusCardDark,
    onSurface = NexusSilverBright,
    surfaceVariant = NexusSurfaceDark,
    onSurfaceVariant = NexusSilver,
    outline = NexusBorderHairline,
    outlineVariant = NexusSilverDark
)

@Composable
fun NexusAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = NexusDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NexusPureBlack.toArgb()
                window.navigationBarColor = NexusPureBlack.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

```

---

### File: `app/src/main/java/com/example/ui/theme/Type.kt`

```kotlin
package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography =
  Typography(
    bodyLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
      )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
  )

```

---

### File: `app/src/main/java/com/example/MainActivity.kt`

```kotlin
package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MemoriesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusAiTheme
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusDeepBlack
import com.example.ui.theme.NexusPureBlack
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium
import com.example.viewmodel.NexusViewModel
import kotlinx.coroutines.launch

enum class NexusTab(val title: String, val icon: ImageVector) {
    CHAT("Core", Icons.Default.SmartToy),
    MEMORIES("Memories", Icons.Default.Memory),
    HISTORY("History", Icons.Default.History),
    SETTINGS("Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    private val viewModel: NexusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusAiTheme {
                NexusApp(viewModel)
            }
        }
    }
}

@Composable
fun NexusApp(viewModel: NexusViewModel) {
    var currentTab by remember { mutableStateOf(NexusTab.CHAT) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Handle back button on sub-screens to return to CHAT
    BackHandler(enabled = currentTab != NexusTab.CHAT || drawerState.isOpen) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            currentTab = NexusTab.CHAT
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = NexusPureBlack,
                drawerContentColor = NexusSilverBright,
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .border(
                        BorderStroke(1.dp, NexusBorderHairline),
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    // Drawer Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp, top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(NexusSurfaceDark)
                                    .border(1.dp, NexusBorderHairline, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = NexusTitanium,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "NEXUS",
                                color = NexusSilverBright,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                        }

                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.close() } },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = NexusSilverMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(NexusBorderHairline)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation Items
                    NexusTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = if (isSelected) NexusSilverBright else NexusSilverMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    color = if (isSelected) NexusSilverBright else NexusSilver,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                currentTab = tab
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = NexusSurfaceElevated,
                                unselectedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .testTag("drawer_${tab.name.lowercase()}")
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Minimalist Footer
                    Text(
                        text = "NEXUS SYSTEM",
                        color = NexusSilverMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = NexusPureBlack,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "TabContentTransition"
                ) { tab ->
                    when (tab) {
                        NexusTab.CHAT -> ChatScreen(
                            viewModel = viewModel,
                            onOpenMenu = { coroutineScope.launch { drawerState.open() } },
                            onNavigateToSettings = { currentTab = NexusTab.SETTINGS }
                        )
                        NexusTab.MEMORIES -> MemoriesScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentTab = NexusTab.CHAT },
                            onOpenMenu = { coroutineScope.launch { drawerState.open() } }
                        )
                        NexusTab.HISTORY -> HistoryScreen(
                            viewModel = viewModel,
                            onSelectConversation = {
                                currentTab = NexusTab.CHAT
                            },
                            onNavigateBack = { currentTab = NexusTab.CHAT },
                            onOpenMenu = { coroutineScope.launch { drawerState.open() } }
                        )
                        NexusTab.SETTINGS -> SettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentTab = NexusTab.CHAT },
                            onOpenMenu = { coroutineScope.launch { drawerState.open() } }
                        )
                    }
                }
            }
        }
    }

    // Global Error Dialog
    errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissError() },
            containerColor = NexusCardDark,
            title = {
                Text(
                    text = "NEXUS ALERT",
                    color = NeonRed,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text(
                    text = error,
                    color = NexusSilverBright,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NexusTitanium,
                        contentColor = NexusDeepBlack
                    )
                ) {
                    Text("Acknowledge", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

```

---

### File: `app/src/main/java/com/example/data/api/GeminiApiModels.kt`

```kotlin
package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "role") val role: String? = null,
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.7f,
    @Json(name = "topP") val topP: Float? = 0.95f,
    @Json(name = "topK") val topK: Int? = 40,
    @Json(name = "maxOutputTokens") val maxOutputTokens: Int? = 2048
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null,
    @Json(name = "error") val error: GeminiErrorResponse? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null,
    @Json(name = "finishReason") val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiErrorResponse(
    @Json(name = "code") val code: Int? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "status") val status: String? = null
)

```

---

### File: `app/src/main/java/com/example/data/api/GeminiApiService.kt`

```kotlin
package com.example.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse

    companion object {
        private const val BASE_URL = "https://generativelanguage.googleapis.com/"

        fun create(): GeminiApiService {
            // Strict security: ensure no headers or API keys are logged to logcat
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            }

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(GeminiApiService::class.java)
        }
    }
}

```

---

### File: `app/src/main/java/com/example/data/model/ChatMessage.kt`

```kotlin
package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["conversationId"])]
)
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationId: Long,
    val sender: String, // "user" or "nexus"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val language: String? = null, // "en", "hi", etc.
    val isError: Boolean = false
)

```

---

### File: `app/src/main/java/com/example/data/model/Conversation.kt`

```kotlin
package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val snippet: String = ""
)

```

---

### File: `app/src/main/java/com/example/data/model/NexusMemory.kt`

```kotlin
package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nexus_memories")
data class NexusMemory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val key: String, // Short label e.g., "User's Name", "Language Preference", "Interests"
    val fact: String, // Detailed fact e.g., "User prefers answers in Hindi and English", "Himanshu"
    val category: String = "General", // "Personal", "Preference", "Work", "General"
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

```

---

### File: `app/src/main/java/com/example/data/db/ChatDao.kt`

```kotlin
package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: Long): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessagesList(conversationId: Long): List<ChatMessage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessageById(messageId: Long)

    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun clearMessagesForConversation(conversationId: Long)
}

```

---

### File: `app/src/main/java/com/example/data/db/ConversationDao.kt`

```kotlin
package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun getAllConversations(): Flow<List<Conversation>>

    @Query("SELECT * FROM conversations WHERE id = :id LIMIT 1")
    suspend fun getConversationById(id: Long): Conversation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation): Long

    @Update
    suspend fun updateConversation(conversation: Conversation)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversationById(id: Long)

    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()
}

```

---

### File: `app/src/main/java/com/example/data/db/MemoryDao.kt`

```kotlin
package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.NexusMemory
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM nexus_memories ORDER BY createdAt DESC")
    fun getAllMemories(): Flow<List<NexusMemory>>

    @Query("SELECT * FROM nexus_memories WHERE isActive = 1 ORDER BY createdAt DESC")
    suspend fun getActiveMemoriesList(): List<NexusMemory>

    @Query("SELECT COUNT(*) FROM nexus_memories")
    suspend fun getMemoryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: NexusMemory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemories(memories: List<NexusMemory>)

    @Update
    suspend fun updateMemory(memory: NexusMemory)

    @Query("DELETE FROM nexus_memories WHERE id = :id")
    suspend fun deleteMemoryById(id: Long)

    @Query("DELETE FROM nexus_memories")
    suspend fun deleteAllMemories()
}

```

---

### File: `app/src/main/java/com/example/data/db/NexusDatabase.kt`

```kotlin
package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.Conversation
import com.example.data.model.NexusMemory

@Database(
    entities = [
        ChatMessage::class,
        Conversation::class,
        NexusMemory::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NexusDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun conversationDao(): ConversationDao
    abstract fun memoryDao(): MemoryDao

    companion object {
        @Volatile
        private var INSTANCE: NexusDatabase? = null

        fun getInstance(context: Context): NexusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NexusDatabase::class.java,
                    "nexus_ai.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

```

---

### File: `app/src/main/java/com/example/data/repository/NexusRepository.kt`

```kotlin
package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.data.api.GeminiApiService
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiGenerateRequest
import com.example.data.api.GeminiGenerationConfig
import com.example.data.api.GeminiPart
import com.example.data.db.ChatDao
import com.example.data.db.ConversationDao
import com.example.data.db.MemoryDao
import com.example.data.model.ChatMessage
import com.example.data.model.Conversation
import com.example.data.model.NexusMemory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NexusRepository(
    private val context: Context,
    private val chatDao: ChatDao,
    private val conversationDao: ConversationDao,
    private val memoryDao: MemoryDao,
    private val geminiService: GeminiApiService = GeminiApiService.create()
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("nexus_prefs", Context.MODE_PRIVATE)

    companion object {
        const val PREF_CUSTOM_API_KEY = "pref_custom_api_key"
        const val PREF_SELECTED_MODEL = "pref_selected_model"
        const val PREF_LANGUAGE_MODE = "pref_language_mode"
        const val PREF_PERSONA_STYLE = "pref_persona_style"
        const val PREF_AUTO_SPEAK = "pref_auto_speak"
        const val PREF_TTS_SPEED = "pref_tts_speed"
        const val PREF_TTS_PITCH = "pref_tts_pitch"
        const val DEFAULT_MODEL = "gemini-3.5-flash"
        const val FALLBACK_MODEL = "gemini-3.5-flash"
    }

    // Conversations Flow
    val allConversations: Flow<List<Conversation>> = conversationDao.getAllConversations()

    // Memories Flow
    val allMemories: Flow<List<NexusMemory>> = memoryDao.getAllMemories()

    fun getMessagesForConversation(conversationId: Long): Flow<List<ChatMessage>> =
        chatDao.getMessagesForConversation(conversationId)

    suspend fun getActiveMemories(): List<NexusMemory> = withContext(Dispatchers.IO) {
        memoryDao.getActiveMemoriesList()
    }

    suspend fun seedInitialMemoriesIfEmpty() = withContext(Dispatchers.IO) {
        if (memoryDao.getMemoryCount() == 0) {
            val seeds = listOf(
                NexusMemory(
                    key = "Bilingual Fluency",
                    fact = "User communicates in both English and Hindi. Reply in Hindi (शुद्ध/सहज हिंदी) when addressed in Hindi, and English when in English.",
                    category = "Language",
                    isActive = true
                ),
                NexusMemory(
                    key = "AI Identity",
                    fact = "You are NEXUS AI: an ultra-modern, sentient-styled personal intelligence with deep reasoning and concise execution.",
                    category = "Personal",
                    isActive = true
                ),
                NexusMemory(
                    key = "Response Tone",
                    fact = "Provide insightful, accurate, and articulate answers. Use clean formatting and bullet points when explaining complex topics.",
                    category = "Preference",
                    isActive = true
                )
            )
            memoryDao.insertMemories(seeds)
        }
    }

    // Official Gemini API Key getter (checks manual override, then managed BuildConfig / ENV)
    fun getApiKey(): String {
        val custom = prefs.getString(PREF_CUSTOM_API_KEY, "")?.trim().orEmpty()
        if (custom.isNotBlank()) return custom

        val envKey = try {
            BuildConfig.ENV_GEMINI_API_KEY.trim()
        } catch (_: Exception) {
            ""
        }
        if (envKey.isNotBlank() && envKey != "MY_GEMINI_API_KEY") return envKey

        val buildKey = try {
            BuildConfig.GEMINI_API_KEY.trim()
        } catch (_: Exception) {
            ""
        }
        return if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") buildKey else ""
    }

    fun isApiKeyConfigured(): Boolean {
        return getApiKey().isNotBlank()
    }

    fun setCustomApiKey(key: String) {
        prefs.edit().putString(PREF_CUSTOM_API_KEY, key.trim()).apply()
    }

    fun sanitizeModelName(model: String): String {
        var m = model.trim()
        if (m.startsWith("models/")) {
            m = m.removePrefix("models/")
        }
        if (m == "gemini-2.5-flash" || m == "gemini-2.0-flash") {
            m = DEFAULT_MODEL
        }
        return m
    }

    fun getSelectedModel(): String {
        val raw = prefs.getString(PREF_SELECTED_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL
        return sanitizeModelName(raw)
    }

    fun setSelectedModel(model: String) {
        val clean = sanitizeModelName(model)
        prefs.edit().putString(PREF_SELECTED_MODEL, clean).apply()
    }

    fun getLanguageMode(): String {
        return prefs.getString(PREF_LANGUAGE_MODE, "Bilingual (Auto)") ?: "Bilingual (Auto)"
    }

    fun setLanguageMode(mode: String) {
        prefs.edit().putString(PREF_LANGUAGE_MODE, mode).apply()
    }

    fun getPersonaStyle(): String {
        return prefs.getString(PREF_PERSONA_STYLE, "Futuristic AI") ?: "Futuristic AI"
    }

    fun setPersonaStyle(style: String) {
        prefs.edit().putString(PREF_PERSONA_STYLE, style).apply()
    }

    fun isAutoSpeakEnabled(): Boolean {
        return prefs.getBoolean(PREF_AUTO_SPEAK, false)
    }

    fun setAutoSpeakEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREF_AUTO_SPEAK, enabled).apply()
    }

    fun getTtsSpeed(): Float = prefs.getFloat(PREF_TTS_SPEED, 1.0f)
    fun setTtsSpeed(speed: Float) = prefs.edit().putFloat(PREF_TTS_SPEED, speed).apply()

    fun getTtsPitch(): Float = prefs.getFloat(PREF_TTS_PITCH, 1.0f)
    fun setTtsPitch(pitch: Float) = prefs.edit().putFloat(PREF_TTS_PITCH, pitch).apply()

    // Conversation management
    suspend fun createConversation(title: String = "New Nexus Session"): Long = withContext(Dispatchers.IO) {
        val conv = Conversation(
            title = title,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            snippet = "Session started"
        )
        conversationDao.insertConversation(conv)
    }

    suspend fun updateConversationTitle(id: Long, title: String) = withContext(Dispatchers.IO) {
        val conv = conversationDao.getConversationById(id)
        if (conv != null) {
            conversationDao.updateConversation(conv.copy(title = title, updatedAt = System.currentTimeMillis()))
        }
    }

    suspend fun deleteConversation(id: Long) = withContext(Dispatchers.IO) {
        conversationDao.deleteConversationById(id)
    }

    suspend fun clearAllConversations() = withContext(Dispatchers.IO) {
        conversationDao.deleteAllConversations()
    }

    // Memory Management
    suspend fun insertMemory(memory: NexusMemory): Long = withContext(Dispatchers.IO) {
        memoryDao.insertMemory(memory)
    }

    suspend fun updateMemory(memory: NexusMemory) = withContext(Dispatchers.IO) {
        memoryDao.updateMemory(memory)
    }

    suspend fun deleteMemory(id: Long) = withContext(Dispatchers.IO) {
        memoryDao.deleteMemoryById(id)
    }

    suspend fun deleteAllMemories() = withContext(Dispatchers.IO) {
        memoryDao.deleteAllMemories()
    }

    // Chat Message DB operations
    suspend fun insertMessage(message: ChatMessage): Long = withContext(Dispatchers.IO) {
        val id = chatDao.insertMessage(message)
        // Update snippet and updatedAt on the conversation
        val conv = conversationDao.getConversationById(message.conversationId)
        if (conv != null) {
            val snippet = if (message.text.length > 60) message.text.take(60) + "..." else message.text
            val newTitle = if (conv.snippet == "Session started" && message.sender == "user") {
                if (message.text.length > 30) message.text.take(30) + "..." else message.text
            } else conv.title

            conversationDao.updateConversation(
                conv.copy(
                    title = newTitle,
                    snippet = snippet,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
        id
    }

    suspend fun deleteMessage(messageId: Long) = withContext(Dispatchers.IO) {
        chatDao.deleteMessageById(messageId)
    }

    // Gemini API Query with Multi-Turn & Memory Injection
    suspend fun askGemini(
        conversationId: Long,
        userPrompt: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please set GEMINI_API_KEY in AI Studio Secrets panel or enter it in App Settings.")
            )
        }

        val activeMemories = memoryDao.getActiveMemoriesList()
        val memoryBlock = if (activeMemories.isNotEmpty()) {
            buildString {
                appendLine("USER PERSONAL MEMORIES AND PREFERENCES:")
                activeMemories.forEach {
                    appendLine("- [${it.category}] ${it.key}: ${it.fact}")
                }
            }
        } else {
            "No specific user memories stored yet."
        }

        val persona = getPersonaStyle()
        val languagePreference = getLanguageMode()

        val systemInstructionText = """
            You are NEXUS AI, an elite, ultra-capable personal artificial intelligence assistant.
            Design & Persona: $persona. You are thoughtful, futuristic, insightful, fast, and helpful.
            
            LANGUAGE CAPABILITY:
            - Full bilingual proficiency in English and Hindi.
            - Current user language setting: $languagePreference.
            - If user speaks or asks in Hindi, answer in fluent, natural, respectful Hindi (Devanagari script or Hinglish if requested).
            - If user speaks in English, reply in polished, articulate English.
            - You can switch effortlessly between Hindi and English (code-switching) whenever natural.
            
            PERSONAL MEMORY SYSTEM:
            $memoryBlock
            
            Always respect and utilize these personal memories seamlessly to personalize your responses.
            Keep formatting clean with clear paragraphs, code blocks, or bullet points. Avoid filler text.
        """.trimIndent()

        // Fetch recent conversation history to provide context (last 10 messages)
        val history = chatDao.getMessagesList(conversationId)
        val recentMessages = history.takeLast(10)

        val contents = mutableListOf<GeminiContent>()
        for (msg in recentMessages) {
            val role = if (msg.sender == "user") "user" else "model"
            if (msg.text.isNotBlank() && !msg.isError) {
                contents.add(
                    GeminiContent(
                        role = role,
                        parts = listOf(GeminiPart(text = msg.text))
                    )
                )
            }
        }

        // Add the current user prompt if not already present in the list
        if (contents.isEmpty() || contents.last().role != "user" || contents.last().parts.firstOrNull()?.text != userPrompt) {
            contents.add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = userPrompt))
                )
            )
        }

        val request = GeminiGenerateRequest(
            contents = contents,
            systemInstruction = GeminiContent(
                role = "system",
                parts = listOf(GeminiPart(text = systemInstructionText))
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.7f,
                topP = 0.95f,
                maxOutputTokens = 2048
            )
        )

        val primaryModel = getSelectedModel()
        var result = executeGeminiRequest(primaryModel, apiKey, request)

        // Automatic fallback if primary model returns 404 (model retired) or 503 (high demand)
        if (result.isFailure && primaryModel != FALLBACK_MODEL) {
            val fallbackResult = executeGeminiRequest(FALLBACK_MODEL, apiKey, request)
            if (fallbackResult.isSuccess) {
                setSelectedModel(FALLBACK_MODEL)
                result = fallbackResult
            }
        }
        result
    }

    private fun sanitizeErrorMessage(rawMessage: String): String {
        return rawMessage.replace(Regex("AIza[0-9A-Za-z\\-_]{20,}"), "[REDACTED_API_KEY]")
    }

    suspend fun executeGeminiRequest(
        model: String,
        apiKey: String,
        request: GeminiGenerateRequest
    ): Result<String> {
        return try {
            val response = geminiService.generateContent(
                model = sanitizeModelName(model),
                apiKey = apiKey,
                request = request
            )

            if (response.error != null) {
                val code = response.error.code ?: 0
                val msg = response.error.message ?: "Gemini API error code $code"
                val sanitized = sanitizeErrorMessage(msg)
                return Result.failure(Exception(sanitized))
            }

            val text = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text

            if (!text.isNullOrBlank()) {
                Result.success(text.trim())
            } else {
                Result.failure(Exception("Nexus received an empty response from Gemini ($model)."))
            }
        } catch (e: retrofit2.HttpException) {
            val errorBody = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
            val code = e.code()
            val parsedMsg = if (!errorBody.isNullOrBlank()) {
                if (errorBody.contains("\"message\":")) {
                    errorBody.substringAfter("\"message\":").substringBefore("\",").trim(' ', '"', '\n', '\r', '\\')
                } else {
                    "HTTP $code: $errorBody"
                }
            } else {
                "HTTP $code (${e.message()})"
            }
            val sanitized = sanitizeErrorMessage(parsedMsg)
            Result.failure(Exception(sanitized))
        } catch (e: Exception) {
            val sanitized = sanitizeErrorMessage(e.message ?: "Network error")
            Result.failure(Exception(sanitized))
        }
    }

    suspend fun verifyConnection(): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("No API key configured."))
        }
        val model = getSelectedModel()
        val testRequest = GeminiGenerateRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = "Ping"))
                )
            ),
            generationConfig = GeminiGenerationConfig(maxOutputTokens = 5)
        )
        val result = executeGeminiRequest(model, apiKey, testRequest)
        if (result.isFailure && model != FALLBACK_MODEL) {
            val fallback = executeGeminiRequest(FALLBACK_MODEL, apiKey, testRequest)
            if (fallback.isSuccess) {
                setSelectedModel(FALLBACK_MODEL)
                return@withContext Result.success(FALLBACK_MODEL)
            }
        }
        if (result.isSuccess) Result.success(model) else result
    }
}

```

---

### File: `app/src/main/java/com/example/viewmodel/NexusViewModel.kt`

```kotlin
package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.NexusDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.Conversation
import com.example.data.model.NexusMemory
import com.example.data.repository.NexusRepository
import com.example.ui.components.OrbState
import com.example.voice.SpeechManager
import com.example.voice.TextToSpeechManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ConnectionState {
    object Checking : ConnectionState()
    data class Online(val model: String) : ConnectionState()
    object NoKey : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

class NexusViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NexusDatabase.getInstance(application)
    val repository = NexusRepository(
        context = application,
        chatDao = database.chatDao(),
        conversationDao = database.conversationDao(),
        memoryDao = database.memoryDao()
    )

    val speechManager = SpeechManager(application)
    val ttsManager = TextToSpeechManager(application)

    // Current Conversation ID
    private val _currentConversationId = MutableStateFlow<Long>(0L)
    val currentConversationId: StateFlow<Long> = _currentConversationId.asStateFlow()

    // Conversations List
    val conversations: StateFlow<List<Conversation>> = repository.allConversations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Messages for current conversation
    val currentMessages: StateFlow<List<ChatMessage>> = _currentConversationId
        .flatMapLatest { convId ->
            repository.getMessagesForConversation(convId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Memories List
    val memories: StateFlow<List<NexusMemory>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Input & State
    val inputText = MutableStateFlow("")

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val isApiKeyConfigured = MutableStateFlow(repository.isApiKeyConfigured())
    val selectedModel = MutableStateFlow(repository.getSelectedModel())
    val languageMode = MutableStateFlow(repository.getLanguageMode())
    val personaStyle = MutableStateFlow(repository.getPersonaStyle())
    val isAutoSpeak = MutableStateFlow(repository.isAutoSpeakEnabled())
    val ttsSpeed = MutableStateFlow(repository.getTtsSpeed())
    val ttsPitch = MutableStateFlow(repository.getTtsPitch())

    // Accurate Connection State (Tested via Live API Ping)
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Checking)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private var lastPrompt: String? = null

    // Dynamic Orb State
    val orbState: StateFlow<OrbState> = combine(
        _isGenerating,
        speechManager.speechState,
        ttsManager.isSpeaking,
        _errorMessage
    ) { generating, speechState, speaking, error ->
        when {
            error != null -> OrbState.ERROR
            generating -> OrbState.THINKING
            speechState is SpeechManager.SpeechState.Listening -> OrbState.LISTENING
            speaking -> OrbState.SPEAKING
            else -> OrbState.IDLE
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OrbState.IDLE)

    init {
        viewModelScope.launch {
            repository.seedInitialMemoriesIfEmpty()
            // Initialize or load latest conversation
            conversations.collect { convList ->
                if (_currentConversationId.value == 0L) {
                    if (convList.isNotEmpty()) {
                        _currentConversationId.value = convList.first().id
                    } else {
                        val newId = repository.createConversation("Session #1")
                        _currentConversationId.value = newId
                    }
                }
            }
        }

        // Check connection on startup
        checkConnection()

        // Observe speech recognition
        viewModelScope.launch {
            speechManager.speechState.collect { state ->
                when (state) {
                    is SpeechManager.SpeechState.PartialResult -> {
                        inputText.value = state.text
                    }
                    is SpeechManager.SpeechState.FinalResult -> {
                        inputText.value = state.text
                        sendMessage(state.text)
                        speechManager.resetState()
                    }
                    is SpeechManager.SpeechState.Error -> {
                        _errorMessage.value = state.message
                    }
                    else -> {}
                }
            }
        }
    }

    fun checkConnection() {
        isApiKeyConfigured.value = repository.isApiKeyConfigured()
        if (!repository.isApiKeyConfigured()) {
            _connectionState.value = ConnectionState.NoKey
            return
        }
        _connectionState.value = ConnectionState.Checking
        viewModelScope.launch {
            val result = repository.verifyConnection()
            result.onSuccess { activeModel ->
                _connectionState.value = ConnectionState.Online(activeModel)
                selectedModel.value = repository.getSelectedModel()
            }.onFailure { err ->
                val msg = err.message ?: "Connection test failed"
                _connectionState.value = ConnectionState.Error(msg)
            }
        }
    }

    fun startListening() {
        val lang = languageMode.value
        speechManager.startListening(lang)
    }

    fun stopListening() {
        speechManager.stopListening()
    }

    fun toggleListening() {
        if (speechManager.speechState.value is SpeechManager.SpeechState.Listening) {
            stopListening()
        } else {
            startListening()
        }
    }

    fun speakMessage(message: ChatMessage) {
        if (ttsManager.currentlySpeakingId.value == message.id) {
            ttsManager.stop()
        } else {
            ttsManager.speak(
                text = message.text,
                messageId = message.id,
                speed = ttsSpeed.value,
                pitch = ttsPitch.value
            )
        }
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    fun sendMessage(textOverride: String? = null) {
        val text = (textOverride ?: inputText.value).trim()
        if (text.isBlank() || _isGenerating.value) return

        val convId = _currentConversationId.value
        if (convId == 0L) return

        lastPrompt = text
        inputText.value = ""
        _isGenerating.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            // Save user message
            repository.insertMessage(
                ChatMessage(
                    conversationId = convId,
                    sender = "user",
                    text = text,
                    language = languageMode.value
                )
            )

            // Query Gemini
            val result = repository.askGemini(convId, text)
            _isGenerating.value = false

            result.onSuccess { aiReply ->
                val msgId = repository.insertMessage(
                    ChatMessage(
                        conversationId = convId,
                        sender = "nexus",
                        text = aiReply,
                        language = languageMode.value
                    )
                )

                // Update connection state to verified online
                _connectionState.value = ConnectionState.Online(repository.getSelectedModel())

                if (isAutoSpeak.value) {
                    ttsManager.speak(
                        text = aiReply,
                        messageId = msgId,
                        speed = ttsSpeed.value,
                        pitch = ttsPitch.value
                    )
                }
            }.onFailure { error ->
                val errorText = error.message ?: "An unexpected error occurred."
                _errorMessage.value = errorText
                _connectionState.value = ConnectionState.Error(errorText)
                repository.insertMessage(
                    ChatMessage(
                        conversationId = convId,
                        sender = "nexus",
                        text = "⚠️ [SYSTEM ALERT]: $errorText",
                        isError = true
                    )
                )
            }
        }
    }

    fun retryLastMessage() {
        lastPrompt?.let { prompt ->
            _errorMessage.value = null
            sendMessage(prompt)
        }
    }

    fun createNewSession() {
        viewModelScope.launch {
            val count = conversations.value.size + 1
            val newId = repository.createConversation("Nexus Session #$count")
            _currentConversationId.value = newId
            ttsManager.stop()
        }
    }

    fun selectConversation(id: Long) {
        _currentConversationId.value = id
        ttsManager.stop()
    }

    fun deleteConversation(id: Long) {
        viewModelScope.launch {
            repository.deleteConversation(id)
            if (_currentConversationId.value == id) {
                val remaining = conversations.value.filter { it.id != id }
                if (remaining.isNotEmpty()) {
                    _currentConversationId.value = remaining.first().id
                } else {
                    val newId = repository.createConversation("Nexus Session #1")
                    _currentConversationId.value = newId
                }
            }
        }
    }

    fun clearAllConversations() {
        viewModelScope.launch {
            repository.clearAllConversations()
            val newId = repository.createConversation("Nexus Session #1")
            _currentConversationId.value = newId
        }
    }

    fun deleteMessage(messageId: Long) {
        viewModelScope.launch {
            repository.deleteMessage(messageId)
        }
    }

    // Memories
    fun addMemory(key: String, fact: String, category: String) {
        viewModelScope.launch {
            repository.insertMemory(
                NexusMemory(
                    key = key.trim(),
                    fact = fact.trim(),
                    category = category,
                    isActive = true
                )
            )
        }
    }

    fun updateMemory(memory: NexusMemory) {
        viewModelScope.launch {
            repository.updateMemory(memory)
        }
    }

    fun deleteMemory(id: Long) {
        viewModelScope.launch {
            repository.deleteMemory(id)
        }
    }

    fun toggleMemoryActive(memory: NexusMemory) {
        viewModelScope.launch {
            repository.updateMemory(memory.copy(isActive = !memory.isActive))
        }
    }

    // Settings
    fun updateCustomApiKey(key: String) {
        repository.setCustomApiKey(key)
        isApiKeyConfigured.value = repository.isApiKeyConfigured()
        checkConnection()
    }

    fun updateModel(model: String) {
        repository.setSelectedModel(model)
        selectedModel.value = repository.getSelectedModel()
        checkConnection()
    }

    fun updateLanguageMode(mode: String) {
        repository.setLanguageMode(mode)
        languageMode.value = mode
    }

    fun updatePersonaStyle(style: String) {
        repository.setPersonaStyle(style)
        personaStyle.value = style
    }

    fun updateAutoSpeak(enabled: Boolean) {
        repository.setAutoSpeakEnabled(enabled)
        isAutoSpeak.value = enabled
    }

    fun updateTtsSettings(speed: Float, pitch: Float) {
        repository.setTtsSpeed(speed)
        repository.setTtsPitch(pitch)
        ttsSpeed.value = speed
        ttsPitch.value = pitch
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.stopListening()
        ttsManager.shutdown()
    }
}

```

---

### File: `app/src/main/java/com/example/voice/SpeechManager.kt`

```kotlin
package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechManager(private val context: Context) {

    sealed class SpeechState {
        object Idle : SpeechState()
        object Listening : SpeechState()
        data class PartialResult(val text: String) : SpeechState()
        data class FinalResult(val text: String) : SpeechState()
        data class Error(val message: String) : SpeechState()
    }

    private val _speechState = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val speechState: StateFlow<SpeechState> = _speechState.asStateFlow()

    private val _soundLevel = MutableStateFlow(0f)
    val soundLevel: StateFlow<Float> = _soundLevel.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    val isRecognitionAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(context)

    fun startListening(languagePreference: String = "en-US") {
        if (!isRecognitionAvailable) {
            _speechState.value = SpeechState.Error("Speech recognition is not available on this device.")
            return
        }

        stopListening()

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _speechState.value = SpeechState.Listening
                    }

                    override fun onBeginningOfSpeech() {
                        _speechState.value = SpeechState.Listening
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        // Normalize -2dB..10dB to 0..1f
                        val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                        _soundLevel.value = normalized
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        _soundLevel.value = 0f
                    }

                    override fun onError(error: Int) {
                        val message = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                            SpeechRecognizer.ERROR_CLIENT -> "Client recognition error"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required"
                            SpeechRecognizer.ERROR_NETWORK -> "Network error during speech recognition"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized. Try speaking closer."
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech recognizer is busy"
                            SpeechRecognizer.ERROR_SERVER -> "Server error"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                            else -> "Recognition error #$error"
                        }
                        _soundLevel.value = 0f
                        _speechState.value = SpeechState.Error(message)
                    }

                    override fun onResults(results: Bundle?) {
                        _soundLevel.value = 0f
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotBlank()) {
                            _speechState.value = SpeechState.FinalResult(text)
                        } else {
                            _speechState.value = SpeechState.Idle
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotBlank()) {
                            _speechState.value = SpeechState.PartialResult(text)
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                
                val langTag = when (languagePreference.lowercase()) {
                    "hindi", "hi", "hi-in" -> "hi-IN"
                    "bilingual (auto)" -> Locale.getDefault().toLanguageTag()
                    else -> "en-US"
                }
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, langTag)
            }

            speechRecognizer?.startListening(intent)
            _speechState.value = SpeechState.Listening
        } catch (e: Exception) {
            _speechState.value = SpeechState.Error("Failed to initiate voice input: ${e.message}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
        _soundLevel.value = 0f
        if (_speechState.value is SpeechState.Listening) {
            _speechState.value = SpeechState.Idle
        }
    }

    fun resetState() {
        _speechState.value = SpeechState.Idle
        _soundLevel.value = 0f
    }
}

```

---

### File: `app/src/main/java/com/example/voice/TextToSpeechManager.kt`

```kotlin
package com.example.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentlySpeakingId = MutableStateFlow<Long?>(null)
    val currentlySpeakingId: StateFlow<Long?> = _currentlySpeakingId.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpeakingId.value = null
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentlySpeakingId.value = null
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                    _currentlySpeakingId.value = null
                }
            })
        }
    }

    fun speak(text: String, messageId: Long? = null, speed: Float = 1.0f, pitch: Float = 1.0f) {
        if (!isInitialized || tts == null) return

        stop()

        val cleanText = cleanMarkdownForSpeech(text)
        if (cleanText.isBlank()) return

        // Detect Hindi characters (Devanagari Unicode block \u0900-\u097F)
        val hasHindi = cleanText.any { it.code in 0x0900..0x097F }
        val targetLocale = if (hasHindi) Locale("hi", "IN") else Locale.US

        try {
            tts?.language = targetLocale
            tts?.setSpeechRate(speed.coerceIn(0.5f, 2.0f))
            tts?.setPitch(pitch.coerceIn(0.5f, 2.0f))

            _currentlySpeakingId.value = messageId
            val utteranceId = "utterance_${messageId ?: System.currentTimeMillis()}"
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
            _isSpeaking.value = true
        } catch (_: Exception) {
            _isSpeaking.value = false
            _currentlySpeakingId.value = null
        }
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _isSpeaking.value = false
        _currentlySpeakingId.value = null
    }

    fun shutdown() {
        stop()
        try {
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isInitialized = false
    }

    private fun cleanMarkdownForSpeech(raw: String): String {
        return raw
            // Remove code blocks
            .replace(Regex("```[\\s\\S]*?```"), "Code omitted.")
            // Remove inline code
            .replace(Regex("`([^`]+)`"), "$1")
            // Remove markdown links [title](url) -> title
            .replace(Regex("\\[([^\\]]+)\\]\\([^)]+\\)"), "$1")
            // Remove headers #, ##, etc.
            .replace(Regex("#{1,6}\\s*"), "")
            // Remove bold/italics
            .replace(Regex("[*_~]{1,3}"), "")
            // Remove blockquotes
            .replace(Regex("^>\\s*", RegexOption.MULTILINE), "")
            // Replace multiple newlines with period
            .replace(Regex("\n+"), ". ")
            .trim()
    }
}

```

---

### File: `app/src/main/java/com/example/ui/components/CommonComponents.kt`

```kotlin
package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium

fun Modifier.cyberBorder(
    color: Color = NexusBorderHairline,
    width: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(12.dp)
) = this
    .border(width = width, color = color, shape = shape)
    .clip(shape)

@Composable
fun CyberStatusBadge(
    text: String,
    statusColor: Color = NeonEmerald,
    icon: ImageVector = Icons.Default.Circle
) {
    Surface(
        color = NexusSurfaceDark,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(statusColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text.uppercase(),
                color = NexusSilverBright,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Minimalist connection indicator light.
 * Green when connected, Red when disconnected/no key, Grey while checking.
 * No text labels, strictly a subtle luminous status light.
 */
@Composable
fun NexusConnectionIndicator(
    state: com.example.viewmodel.ConnectionState,
    onIndicatorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (indicatorColor, isChecking) = when (state) {
        is com.example.viewmodel.ConnectionState.Online -> Color(0xFF10B981) to false
        is com.example.viewmodel.ConnectionState.Checking -> Color(0xFF64748B) to true
        is com.example.viewmodel.ConnectionState.Error,
        is com.example.viewmodel.ConnectionState.NoKey -> Color(0xFFEF4444) to false
    }

    val infiniteTransition = rememberInfiniteTransition(label = "indicatorTransition")
    val checkingAlpha = if (isChecking) {
        val anim by infiniteTransition.animateFloat(
            initialValue = 0.35f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(750),
                repeatMode = RepeatMode.Reverse
            ),
            label = "checkingAlpha"
        )
        anim
    } else {
        1.0f
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onIndicatorClick),
        contentAlignment = Alignment.Center
    ) {
        // Outer soft glow halo
        Box(
            modifier = Modifier
                .size(15.dp)
                .background(indicatorColor.copy(alpha = 0.22f * checkingAlpha), CircleShape)
        )
        // Core luminous point
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(indicatorColor.copy(alpha = checkingAlpha), CircleShape)
        )
    }
}

@Composable
fun ApiKeyWarningBanner(
    onConfigureClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        color = NexusCardDark,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Key,
                contentDescription = null,
                tint = NeonRed,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Gemini API Key Required",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Configure in AI Studio Secrets or enter key in Settings.",
                    color = NexusSilver,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onConfigureClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusSilverBright),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusSilverDark)
            ) {
                Text("Setup", fontSize = 11.5.sp)
            }
        }
    }
}

@Composable
fun CyberChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NexusBorderHairline
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = NexusSurfaceDark,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = NexusTitanium,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

```

---

### File: `app/src/main/java/com/example/ui/components/NexusOrb.kt`

```kotlin
package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.ParticleCyan
import com.example.ui.theme.ParticleSilver
import com.example.ui.theme.ParticleTitanium
import com.example.ui.theme.ParticleViolet
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

enum class OrbState {
    IDLE,
    LISTENING,
    THINKING,
    SPEAKING,
    ERROR
}

/**
 * Pre-calculated point on unit sphere with coordinates and metadata
 */
private class FinePoint(
    val x0: Float,
    val y0: Float,
    val z0: Float,
    val phi: Float,
    val lambda: Float,
    val layer: Int, // 0 = fine wave shell, 1 = dense core stardust, 2 = orbital stream
    val highlightType: Int // 0 = silver, 1 = cyan, 2 = titanium, 3 = violet
)

@Composable
fun NexusOrb(
    modifier: Modifier = Modifier,
    orbState: OrbState = OrbState.IDLE,
    soundLevel: Float = 0f,
    orbSize: Dp = 230.dp,
    onClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val touchPulse = remember { Animatable(0f) }

    // Parallax interactive 3D rotation angles
    var dragYaw by remember { mutableFloatStateOf(0f) }
    var dragPitch by remember { mutableFloatStateOf(0f) }

    // Precomputed fine particle cloud (~2,960 dots using Fibonacci distribution)
    val particles = remember {
        val list = ArrayList<FinePoint>(2960)
        val goldenRatio = (1f + sqrt(5f)) / 2f
        val goldenAngle = (2f * PI * (1f - 1f / goldenRatio)).toFloat()

        // Layer 0: High-density fine wave shell (2,400 points)
        val countShell = 2400
        for (i in 0 until countShell) {
            val y0 = 1f - (i.toFloat() / (countShell - 1f)) * 2f
            val rCirc = sqrt((1f - y0 * y0).coerceAtLeast(0f))
            val theta = goldenAngle * i
            val x0 = cos(theta) * rCirc
            val z0 = sin(theta) * rCirc
            val phi = atan2(z0, x0)
            val lambda = kotlin.math.asin(y0.coerceIn(-1f, 1f))
            val highlight = when {
                i % 18 == 0 -> 1 // Restrained cyan highlight
                i % 29 == 0 -> 3 // Subtle violet glint
                i % 6 == 0 -> 2  // Brushed titanium
                else -> 0        // Pure metallic silver
            }
            list.add(FinePoint(x0, y0, z0, phi, lambda, layer = 0, highlightType = highlight))
        }

        // Layer 1: Inner nucleus stardust (320 points)
        val countInner = 320
        for (i in 0 until countInner) {
            val y0 = 1f - (i.toFloat() / (countInner - 1f)) * 2f
            val rCirc = sqrt((1f - y0 * y0).coerceAtLeast(0f))
            val theta = goldenAngle * (i + 1200)
            val radInner = 0.38f + 0.12f * sin(i.toFloat() * 0.7f)
            val x0 = cos(theta) * rCirc * radInner
            val z0 = sin(theta) * rCirc * radInner
            val highlight = if (i % 12 == 0) 1 else 0
            list.add(FinePoint(x0, y0 * radInner, z0, theta, y0, layer = 1, highlightType = highlight))
        }

        // Layer 2: Orbital fine harmonic ring (240 points)
        val countRing = 240
        for (i in 0 until countRing) {
            val angle = (2f * PI * i / countRing).toFloat()
            val rSpread = 1.16f + 0.05f * sin(angle * 4f)
            val tilt = (22f * PI / 180f).toFloat()
            val x0 = cos(angle) * rSpread
            val y0 = sin(angle) * sin(tilt) * rSpread
            val z0 = sin(angle) * cos(tilt) * rSpread
            val highlight = if (i % 10 == 0) 1 else 2
            list.add(FinePoint(x0, y0, z0, angle, tilt, layer = 2, highlightType = highlight))
        }

        list
    }

    val infiniteTransition = rememberInfiniteTransition(label = "NexusOrbTransition")

    // State-based animation speeds
    val rotationPeriod = when (orbState) {
        OrbState.THINKING -> 3200
        OrbState.LISTENING -> 6800
        OrbState.SPEAKING -> 4800
        OrbState.ERROR -> 11000
        OrbState.IDLE -> 9000
    }

    val continuousYaw by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = rotationPeriod, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "continuousYaw"
    )

    val surfaceWavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (orbState == OrbState.THINKING) 1700 else 4200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "surfaceWavePhase"
    )

    val breathingCycle by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (orbState == OrbState.SPEAKING) 750 else 2200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingCycle"
    )

    Box(
        modifier = modifier
            .size(orbSize)
            .pointerInput(Unit) {
                detectTapGestures {
                    coroutineScope.launch {
                        touchPulse.snapTo(0f)
                        touchPulse.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
                        touchPulse.snapTo(0f)
                    }
                    onClick()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragYaw = (dragYaw + dragAmount.x * 0.007f)
                    dragPitch = (dragPitch - dragAmount.y * 0.007f).coerceIn(-1.1f, 1.1f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = (size.minDimension / 2f) * 0.44f

            // Total 3D viewing angles with drag parallax
            val totalYaw = continuousYaw + dragYaw
            val totalPitch = (0.24f * sin(continuousYaw * 0.5f) + dragPitch).toFloat()

            // 1. Volumetric Ambient Glow Backdrop (Restrained dark graphite & titanium glow)
            drawVolumetricAura(center, baseRadius, orbState)

            // 2. Faint Metallic Reticles (Subtle hairline orbits)
            drawMetallicReticles(center, baseRadius, totalYaw, orbState)

            // 3. Audio & Tap Harmonic Wave Ripples
            if (orbState == OrbState.SPEAKING || orbState == OrbState.LISTENING || touchPulse.value > 0f) {
                drawWaveHarmonics(center, baseRadius, surfaceWavePhase, soundLevel, touchPulse.value, orbState)
            }

            // 4. Render 3D Stardust Particle Waveform
            draw3DFineParticleCloud(
                particles = particles,
                center = center,
                baseRadius = baseRadius,
                yaw = totalYaw,
                pitch = totalPitch,
                wavePhase = surfaceWavePhase,
                breathing = breathingCycle,
                soundLevel = soundLevel,
                touchPulse = touchPulse.value,
                orbState = orbState
            )
        }
    }
}

// 1. Soft Volumetric Backdrop (Subtle deep graphite / charcoal aura)
private fun DrawScope.drawVolumetricAura(
    center: Offset,
    radius: Float,
    state: OrbState
) {
    val auraColor = when (state) {
        OrbState.ERROR -> NeonRed
        OrbState.LISTENING -> NeonEmerald
        OrbState.THINKING -> ParticleViolet
        OrbState.SPEAKING -> ParticleCyan
        OrbState.IDLE -> Color(0xFF64748B)
    }

    val glowRadius = radius * 1.9f
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0.0f to auraColor.copy(alpha = 0.12f),
                0.35f to auraColor.copy(alpha = 0.04f),
                0.7f to Color(0xFF0F172A).copy(alpha = 0.015f),
                1.0f to Color.Transparent
            ),
            center = center,
            radius = glowRadius
        ),
        radius = glowRadius,
        center = center
    )
}

// 2. Subtle Metallic Titanium Hairline Orbits
private fun DrawScope.drawMetallicReticles(
    center: Offset,
    radius: Float,
    yaw: Float,
    state: OrbState
) {
    val ringColor = when (state) {
        OrbState.ERROR -> NeonRed.copy(alpha = 0.20f)
        OrbState.LISTENING -> NeonEmerald.copy(alpha = 0.20f)
        else -> Color(0xFF334155).copy(alpha = 0.28f)
    }

    // Outer faint metallic hairline orbit
    drawCircle(
        color = ringColor,
        radius = radius * 1.32f,
        center = center,
        style = Stroke(
            width = 0.8.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 14f), yaw * 12f)
        )
    )

    // Inner fine concentric guide
    drawCircle(
        color = Color(0xFF1E293B).copy(alpha = 0.40f),
        radius = radius * 1.14f,
        center = center,
        style = Stroke(width = 0.6.dp.toPx())
    )
}

// 3. Audio & Tap Harmonic Wave Ripples
private fun DrawScope.drawWaveHarmonics(
    center: Offset,
    radius: Float,
    phase: Float,
    soundLevel: Float,
    tapProgress: Float,
    state: OrbState
) {
    val waveColor = when (state) {
        OrbState.LISTENING -> NeonEmerald
        OrbState.SPEAKING -> ParticleCyan
        OrbState.ERROR -> NeonRed
        else -> ParticleSilver
    }

    if (state == OrbState.SPEAKING || state == OrbState.LISTENING) {
        val count = 3
        for (i in 0 until count) {
            val fraction = ((phase / (2 * PI).toFloat()) + (i.toFloat() / count)) % 1f
            val rippleRadius = radius * (1.02f + fraction * 0.90f)
            val alpha = (1f - fraction) * (0.20f + soundLevel.coerceIn(0f, 1f) * 0.35f)
            drawCircle(
                color = waveColor.copy(alpha = alpha.coerceIn(0f, 0.60f)),
                radius = rippleRadius,
                center = center,
                style = Stroke(width = 0.9.dp.toPx() * (1f - fraction * 0.5f))
            )
        }
    }

    if (tapProgress > 0f) {
        val tapRadius = radius * (1.0f + tapProgress * 1.35f)
        val tapAlpha = (1f - tapProgress).coerceIn(0f, 1f) * 0.55f
        drawCircle(
            color = ParticleSilver.copy(alpha = tapAlpha),
            radius = tapRadius,
            center = center,
            style = Stroke(width = 1.2.dp.toPx() * (1f - tapProgress * 0.6f))
        )
    }
}

// 4. Primary 3D Fine Particle Waveform (70-85% smaller particles, high density stardust)
private fun DrawScope.draw3DFineParticleCloud(
    particles: List<FinePoint>,
    center: Offset,
    baseRadius: Float,
    yaw: Float,
    pitch: Float,
    wavePhase: Float,
    breathing: Float,
    soundLevel: Float,
    touchPulse: Float,
    orbState: OrbState
) {
    val cosYaw = cos(yaw)
    val sinYaw = sin(yaw)
    val cosPitch = cos(pitch)
    val sinPitch = sin(pitch)

    val camDist = baseRadius * 3.4f

    // Wave amplitude based on state
    val waveAmp = when (orbState) {
        OrbState.THINKING -> 0.16f
        OrbState.LISTENING -> 0.10f + soundLevel.coerceIn(0f, 1f) * 0.18f
        OrbState.SPEAKING -> 0.12f + soundLevel.coerceIn(0f, 1f) * 0.22f
        OrbState.ERROR -> 0.15f
        OrbState.IDLE -> 0.07f
    }

    val stateRadialScale = when (orbState) {
        OrbState.LISTENING -> 0.93f // Inward magnetic flow
        OrbState.SPEAKING -> 1.05f  // Expanding circular waves
        OrbState.THINKING -> 1.02f
        else -> 1.0f
    }

    val effectiveBaseRadius = baseRadius * breathing * stateRadialScale

    // Lighting vector for metallic grey specular reflection
    val lx = 0.35f
    val ly = 0.45f
    val lz = 0.82f

    val shellBaseRadiusPx = 0.22.dp.toPx()
    val shellVarRadiusPx = 0.44.dp.toPx()
    val nucleusBaseRadiusPx = 0.28.dp.toPx()
    val nucleusVarRadiusPx = 0.38.dp.toPx()
    val ringBaseRadiusPx = 0.18.dp.toPx()
    val ringVarRadiusPx = 0.30.dp.toPx()

    for (p in particles) {
        // Fluid 3D spherical harmonics wave equations
        val w1 = sin(3.2f * p.phi + wavePhase * 1.3f) * cos(2.6f * p.lambda)
        val w2 = sin(5.4f * p.lambda - wavePhase * 1.9f) * cos(3.2f * p.phi)
        val w3 = cos(4.0f * (p.x0 + p.y0 + p.z0) + wavePhase * 0.9f)
        val surfaceDisplacement = (0.50f * w1 + 0.30f * w2 + 0.20f * w3) * waveAmp

        val touchDisplacement = if (touchPulse > 0f && p.layer == 0) {
            0.22f * sin(touchPulse * PI.toFloat())
        } else 0f

        val r = when (p.layer) {
            0 -> effectiveBaseRadius * (1f + surfaceDisplacement + touchDisplacement)
            1 -> effectiveBaseRadius * 0.50f * (1f + surfaceDisplacement * 0.35f)
            else -> effectiveBaseRadius * (1.16f + 0.04f * sin(p.phi * 4f + wavePhase))
        }

        // Cartesian 3D coordinates
        val x3d = p.x0 * r
        val y3d = p.y0 * r
        val z3d = p.z0 * r

        // 3D Rotation (Yaw + Pitch)
        val xRot = x3d * cosYaw + z3d * sinYaw
        val zTemp = -x3d * sinYaw + z3d * cosYaw
        val yRot = y3d * cosPitch - zTemp * sinPitch
        val zRot = y3d * sinPitch + zTemp * cosPitch

        // Perspective scale & screen projection
        val perspective = camDist / (camDist - zRot)
        val screenX = center.x + xRot * perspective
        val screenY = center.y + yRot * perspective

        // Depth factor [0 = deep back, 1 = front closest]
        val depthNorm = ((zRot + baseRadius) / (2f * baseRadius)).coerceIn(0f, 1f)

        // Ultra-fine particle radius (70-85% size reduction)
        val dotRadius = when (p.layer) {
            1 -> (nucleusBaseRadiusPx + depthNorm * nucleusVarRadiusPx) * perspective
            2 -> (ringBaseRadiusPx + depthNorm * ringVarRadiusPx) * perspective
            else -> (shellBaseRadiusPx + depthNorm * shellVarRadiusPx) * perspective
        }

        // Metallic reflection calculation
        val nx = p.x0 * cosYaw + p.z0 * sinYaw
        val nz = -p.x0 * sinYaw + p.z0 * cosYaw
        val nyRot = p.y0 * cosPitch - nz * sinPitch
        val nzRot = p.y0 * sinPitch + nz * cosPitch
        val dotProduct = (nx * lx + nyRot * ly + nzRot * lz).coerceIn(0f, 1f)
        val specular = dotProduct.pow(3) * 0.35f

        val baseAlpha = when (p.layer) {
            1 -> 0.30f + depthNorm * 0.55f + specular
            2 -> 0.18f + depthNorm * 0.60f
            else -> 0.14f + depthNorm * 0.72f + specular
        }.coerceIn(0.08f, 0.95f)

        // Determine particle color with subtle metallic highlights
        val dotColor = when {
            orbState == OrbState.ERROR -> {
                if (p.highlightType == 1 || p.highlightType == 3) NeonRed else ParticleSilver
            }
            orbState == OrbState.LISTENING -> {
                if (p.highlightType == 1) NeonEmerald else if (depthNorm > 0.75f) ParticleSilver else ParticleTitanium
            }
            orbState == OrbState.THINKING -> {
                if (p.highlightType == 3) ParticleViolet else if (p.highlightType == 1) ParticleCyan else ParticleSilver
            }
            else -> {
                when (p.highlightType) {
                    1 -> ParticleCyan      // Restrained cyan stardust highlight
                    2 -> ParticleTitanium  // Brushed titanium grey
                    3 -> ParticleViolet    // Subtle violet glint
                    else -> if (specular > 0.2f) Color(0xFFF1F5F9) else ParticleSilver // Metallic silver
                }
            }
        }

        // Draw fine stardust particle
        drawCircle(
            color = dotColor.copy(alpha = baseAlpha),
            radius = dotRadius,
            center = Offset(screenX, screenY)
        )

        // Micro-bloom halo for prominent foreground points (restrained, no large beads)
        if (depthNorm > 0.88f && p.layer == 0) {
            drawCircle(
                color = dotColor.copy(alpha = 0.14f * (depthNorm - 0.88f) * 8.3f),
                radius = dotRadius * 1.8f,
                center = Offset(screenX, screenY)
            )
        }
    }
}

```

---

### File: `app/src/main/java/com/example/ui/screens/ChatScreen.kt`

```kotlin
package com.example.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.components.ApiKeyWarningBanner
import com.example.ui.components.NexusConnectionIndicator
import com.example.ui.components.NexusOrb
import com.example.ui.components.OrbState
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusCharcoal
import com.example.ui.theme.NexusDeepBlack
import com.example.ui.theme.NexusPureBlack
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium
import com.example.ui.theme.ParticleCyan
import com.example.viewmodel.ConnectionState
import com.example.viewmodel.NexusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    viewModel: NexusViewModel,
    onOpenMenu: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val messages by viewModel.currentMessages.collectAsState()
    val orbState by viewModel.orbState.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val isApiKeyConfigured by viewModel.isApiKeyConfigured.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val soundLevel by viewModel.speechManager.soundLevel.collectAsState()
    val speakingId by viewModel.ttsManager.currentlySpeakingId.collectAsState()
    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()

    var showMicRationale by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Permission launcher for RECORD_AUDIO
    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            Toast.makeText(
                context,
                "Microphone permission is required for voice input.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Auto scroll on new message
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
            .imePadding()
    ) {
        // Clean Minimal Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenMenu,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = NexusSilverBright,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NEXUS",
                    color = NexusSilverBright,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSpeaking) {
                    IconButton(
                        onClick = { viewModel.stopSpeaking() },
                        modifier = Modifier
                            .size(36.dp)
                            .background(NexusSurfaceDark, CircleShape)
                            .border(1.dp, NeonRed, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Speaking",
                            tint = NeonRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Small connection indicator light: green when connected, red when offline, grey while checking
                NexusConnectionIndicator(
                    state = connectionState,
                    onIndicatorClick = {
                        if (connectionState is ConnectionState.Error || connectionState is ConnectionState.NoKey) {
                            viewModel.checkConnection()
                        }
                    },
                    modifier = Modifier.testTag("connection_indicator")
                )
            }
        }

        // Api Key Alert if missing
        if (!isApiKeyConfigured || connectionState is ConnectionState.NoKey) {
            ApiKeyWarningBanner(onConfigureClick = onNavigateToSettings)
        }

        // Retry banner if the last message was an error
        val lastMessage = messages.lastOrNull()
        if (lastMessage?.isError == true && !isGenerating) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                color = Color(0xFF1A1114),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Last transmission failed. Tap to retry.",
                        color = Color(0xFFFFB3B8),
                        fontSize = 12.sp
                    )
                    OutlinedButton(
                        onClick = { viewModel.retryLastMessage() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTitanium),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Retry", fontSize = 12.sp)
                    }
                }
            }
        }

        // Center Messages & 3D Particle Core area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Completely clean initial screen: no greetings, no sample prompts, only the 3D particle entity
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NexusOrb(
                        orbState = orbState,
                        soundLevel = soundLevel,
                        orbSize = 250.dp,
                        onClick = {
                            if (orbState == OrbState.LISTENING) {
                                viewModel.stopListening()
                            } else {
                                showMicRationale = true
                            }
                        }
                    )
                }
            } else {
                // Conversation list with compact top particle core bar
                Column(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (orbState == OrbState.LISTENING) {
                                    viewModel.stopListening()
                                } else {
                                    showMicRationale = true
                                }
                            },
                        color = NexusCharcoal.copy(alpha = 0.95f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NexusOrb(
                                orbState = orbState,
                                soundLevel = soundLevel,
                                orbSize = 48.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "NEXUS",
                                color = NexusSilverBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.createNewSession() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Session",
                                    tint = NexusTitanium,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(messages, key = { it.id }) { message ->
                            ChatMessageCard(
                                message = message,
                                isSpeaking = speakingId == message.id,
                                onSpeakClick = { viewModel.speakMessage(message) },
                                onDeleteClick = { viewModel.deleteMessage(message.id) },
                                onRetryClick = { viewModel.retryLastMessage() }
                            )
                        }

                        if (isGenerating) {
                            item {
                                NexusThinkingBubble()
                            }
                        }
                    }
                }
            }
        }

        // Bottom input bar with navigation bar inset padding
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = NexusPureBlack,
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Voice Microphone button
                val isListening = orbState == OrbState.LISTENING
                FloatingActionButton(
                    onClick = {
                        if (isListening) {
                            viewModel.stopListening()
                        } else {
                            showMicRationale = true
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .border(1.dp, if (isListening) ParticleCyan else NexusBorderHairline, CircleShape)
                        .testTag("mic_button"),
                    shape = CircleShape,
                    containerColor = if (isListening) ParticleCyan.copy(alpha = 0.25f) else NexusSurfaceDark,
                    contentColor = if (isListening) ParticleCyan else NexusSilverBright
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.GraphicEq else Icons.Default.Mic,
                        contentDescription = if (isListening) "Stop Listening" else "Start Voice Input",
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Text Input
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { viewModel.inputText.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    placeholder = {
                        Text(
                            "Message NEXUS...",
                            color = NexusSilverMuted,
                            fontSize = 14.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexusSilverBright,
                        unfocusedTextColor = NexusSilverBright,
                        focusedBorderColor = NexusSilver,
                        unfocusedBorderColor = NexusBorderHairline,
                        focusedContainerColor = NexusCardDark,
                        unfocusedContainerColor = NexusCardDark
                    ),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Send Button
                FloatingActionButton(
                    onClick = { viewModel.sendMessage() },
                    modifier = Modifier
                        .size(46.dp)
                        .border(1.dp, NexusBorderHairline, CircleShape)
                        .testTag("send_button"),
                    shape = CircleShape,
                    containerColor = if (inputText.isNotBlank() && !isGenerating) NexusTitanium else NexusSurfaceDark,
                    contentColor = if (inputText.isNotBlank() && !isGenerating) NexusDeepBlack else NexusSilverMuted
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = NexusTitanium,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    // Microphone Permission Rationale Dialog
    if (showMicRationale) {
        AlertDialog(
            onDismissRequest = { showMicRationale = false },
            containerColor = NexusCardDark,
            title = {
                Text(
                    text = "VOICE INPUT ACCESS",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text(
                    text = "NEXUS utilizes Android Speech Recognition to transcribe voice in English and Hindi. Audio is processed locally on device.",
                    color = NexusSilver,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showMicRationale = false
                        micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NexusTitanium, contentColor = NexusDeepBlack)
                ) {
                    Text("Grant Permission", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showMicRationale = false },
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                ) {
                    Text("Cancel", color = NexusSilverBright)
                }
            }
        )
    }
}

@Composable
private fun ChatMessageCard(
    message: ChatMessage,
    isSpeaking: Boolean,
    onSpeakClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val isUser = message.sender == "user"
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeString = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (message.isError) NeonRed.copy(alpha = 0.2f)
                        else NexusSurfaceDark
                    )
                    .border(
                        1.dp,
                        if (message.isError) NeonRed else NexusBorderHairline,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = if (message.isError) NeonRed else NexusTitanium,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            modifier = Modifier
                .widthIn(max = 310.dp)
                .testTag(if (isUser) "user_message_bubble" else "nexus_message_bubble"),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) NexusSurfaceDark
            else if (message.isError) Color(0xFF1E1014)
            else NexusCardDark,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) NexusBorderHairline
                else if (message.isError) NeonRed.copy(alpha = 0.5f)
                else NexusBorderHairline
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header inside bubble
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "YOU" else if (message.isError) "SYSTEM ALERT" else "NEXUS",
                        color = if (isUser) NexusTitanium
                        else if (message.isError) NeonRed
                        else NexusSilverBright,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = timeString,
                        color = NexusSilverMuted,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                SelectionContainer {
                    Text(
                        text = message.text,
                        color = if (message.isError) Color(0xFFFFB3B8) else NexusSilverBright,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                // Retry action button if message failed
                if (message.isError) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onRetryClick,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTitanium),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = NexusTitanium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retry", fontSize = 11.5.sp, color = NexusTitanium)
                        }
                    }
                }

                // Action Bar for Assistant Messages (TTS Speak, Copy)
                if (!isUser && !message.isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Nexus Message", message.text)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy message",
                                tint = NexusSilver,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = onSpeakClick,
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    if (isSpeaking) NexusSurfaceElevated else Color.Transparent,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = if (isSpeaking) "Stop Speech" else "Read Aloud",
                                tint = if (isSpeaking) NexusSilverBright else NexusSilver,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NexusThinkingBubble() {
    val transition = rememberInfiniteTransition(label = "thinking")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "thinkingAlpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = NexusCardDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline.copy(alpha = alpha))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = ParticleCyan,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "NEXUS synthesizing response...",
                    color = NexusSilver,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

```

---

### File: `app/src/main/java/com/example/ui/screens/HistoryScreen.kt`

```kotlin
package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Conversation
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusDeepBlack
import com.example.ui.theme.NexusObsidian
import com.example.ui.theme.NexusPureBlack
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium
import com.example.viewmodel.NexusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: NexusViewModel,
    onSelectConversation: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onOpenMenu: () -> Unit = {}
) {
    val conversations by viewModel.conversations.collectAsState()
    val activeConvId by viewModel.currentConversationId.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var deletingId by remember { mutableStateOf<Long?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val filteredConversations = conversations.filter { conv ->
        searchQuery.isBlank() ||
                conv.title.contains(searchQuery, ignoreCase = true) ||
                conv.snippet.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NexusSilverBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "HISTORY",
                            color = NexusSilverBright,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "${conversations.size} SESSIONS SAVED",
                            color = NexusSilver,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (conversations.isNotEmpty()) {
                        IconButton(
                            onClick = { showClearAllDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear all conversations",
                                tint = NeonRed.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    IconButton(
                        onClick = onOpenMenu,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = NexusSilver,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // New Session Action Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable {
                        viewModel.createNewSession()
                        onSelectConversation(viewModel.currentConversationId.value)
                    },
                color = NexusCardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(NexusSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = NexusTitanium,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Start New Nexus Session",
                                color = NexusSilverBright,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Initialize a fresh context window",
                                color = NexusSilver,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("history_search_field"),
                placeholder = {
                    Text("Search past sessions...", color = NexusSilverMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = NexusSilver
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NexusSilverBright,
                    unfocusedTextColor = NexusSilverBright,
                    focusedBorderColor = NexusSilver,
                    unfocusedBorderColor = NexusBorderHairline,
                    focusedContainerColor = NexusCardDark,
                    unfocusedContainerColor = NexusCardDark
                ),
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )

            // Conversations List
            if (filteredConversations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = NexusSilverDark,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "No sessions found matching '$searchQuery'" else "No conversation logs archived yet",
                            color = NexusSilver,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredConversations, key = { it.id }) { conversation ->
                        val isActive = conversation.id == activeConvId
                        ConversationItemCard(
                            conversation = conversation,
                            isActive = isActive,
                            onClick = {
                                viewModel.selectConversation(conversation.id)
                                onSelectConversation(conversation.id)
                            },
                            onDelete = { deletingId = conversation.id }
                        )
                    }
                }
            }
        }
    }

    // Delete Session Dialog
    deletingId?.let { id ->
        AlertDialog(
            onDismissRequest = { deletingId = null },
            containerColor = NexusCardDark,
            title = {
                Text(
                    text = "DELETE SESSION?",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text(
                    text = "This conversation log and all its messages will be removed permanently.",
                    color = NexusSilver,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteConversation(id)
                        deletingId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { deletingId = null },
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                ) {
                    Text("Cancel", color = NexusSilverBright)
                }
            }
        )
    }

    // Clear All Sessions Dialog
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            containerColor = NexusCardDark,
            title = {
                Text(
                    text = "CLEAR ALL SESSIONS?",
                    color = NeonRed,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text(
                    text = "All saved conversation sessions will be deleted permanently. Personal memories will remain intact.",
                    color = NexusSilver,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllConversations()
                        showClearAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("Clear All", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClearAllDialog = false },
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                ) {
                    Text("Cancel", color = NexusSilverBright)
                }
            }
        )
    }
}

@Composable
private fun ConversationItemCard(
    conversation: Conversation,
    isActive: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(conversation.updatedAt) { dateFormat.format(Date(conversation.updatedAt)) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("conversation_card_${conversation.id}"),
        color = if (isActive) NexusSurfaceDark else NexusCardDark,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) NexusSilver else NexusBorderHairline
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(NexusSurfaceElevated)
                    .border(
                        1.dp,
                        if (isActive) NexusSilver else NexusBorderHairline,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = if (isActive) NexusSilverBright else NexusSilver,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.title,
                        color = NexusSilverBright,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    if (isActive) {
                        Text(
                            text = "CURRENT",
                            color = NexusTitanium,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = conversation.snippet.ifBlank { "No messages yet" },
                    color = NexusSilver,
                    fontSize = 12.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formattedDate,
                    color = NexusSilverMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete session",
                    tint = NexusSilverMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

```

---

### File: `app/src/main/java/com/example/ui/screens/MemoriesScreen.kt`

```kotlin
package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NexusMemory
import com.example.ui.components.CyberChip
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusDeepBlack
import com.example.ui.theme.NexusObsidian
import com.example.ui.theme.NexusPureBlack
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium
import com.example.viewmodel.NexusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onOpenMenu: () -> Unit = {}
) {
    val memories by viewModel.memories.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("All") }

    // Dialog state
    var showAddDialog by remember { mutableStateOf(false) }
    var editingMemory by remember { mutableStateOf<NexusMemory?>(null) }
    var deletingMemoryId by remember { mutableStateOf<Long?>(null) }

    val categories = listOf("All", "Personal", "Language", "Preference", "Work", "General")

    val filteredMemories = memories.filter { mem ->
        val matchesCategory = selectedCategoryFilter == "All" || mem.category.equals(selectedCategoryFilter, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                mem.key.contains(searchQuery, ignoreCase = true) ||
                mem.fact.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NexusSilverBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "MEMORIES",
                            color = NexusSilverBright,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "${memories.count { it.isActive }} ACTIVE CONTEXT NODES",
                            color = NexusSilver,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                IconButton(
                    onClick = onOpenMenu,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = NexusSilver,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Description card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                color = NexusCardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Nexus automatically injects these active memories into every prompt so your assistant remembers your context, name, and language preferences.",
                        color = NexusSilver,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("memory_search_field"),
                placeholder = {
                    Text("Search memories...", color = NexusSilverMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = NexusSilver
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NexusSilverBright,
                    unfocusedTextColor = NexusSilverBright,
                    focusedBorderColor = NexusSilver,
                    unfocusedBorderColor = NexusBorderHairline,
                    focusedContainerColor = NexusCardDark,
                    unfocusedContainerColor = NexusCardDark
                ),
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )

            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    Surface(
                        modifier = Modifier.clickable { selectedCategoryFilter = cat },
                        color = if (isSelected) NexusTitanium else NexusSurfaceDark,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NexusTitanium else NexusBorderHairline
                        )
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) NexusDeepBlack else NexusSilverBright,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Memories List
            if (filteredMemories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            tint = NexusSilverDark,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "No memories matching '$searchQuery'" else "No memories recorded yet",
                            color = NexusSilver,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedButton(
                            onClick = { showAddDialog = true },
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTitanium)
                        ) {
                            Text("Teach Nexus a Fact", color = NexusTitanium)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredMemories, key = { it.id }) { memory ->
                        MemoryItemCard(
                            memory = memory,
                            onToggleActive = { viewModel.toggleMemoryActive(memory) },
                            onEdit = { editingMemory = memory },
                            onDelete = { deletingMemoryId = memory.id }
                        )
                    }
                }
            }
        }

        // Add Memory Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .border(1.dp, NexusBorderHairline, CircleShape)
                .testTag("add_memory_fab"),
            shape = CircleShape,
            containerColor = NexusTitanium,
            contentColor = NexusDeepBlack
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Memory",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Add Memory Dialog
    if (showAddDialog) {
        MemoryEditorDialog(
            title = "ADD NEURAL MEMORY",
            initialKey = "",
            initialFact = "",
            initialCategory = "Personal",
            onDismiss = { showAddDialog = false },
            onConfirm = { key, fact, category ->
                viewModel.addMemory(key, fact, category)
                showAddDialog = false
            }
        )
    }

    // Edit Memory Dialog
    editingMemory?.let { memory ->
        MemoryEditorDialog(
            title = "EDIT NEURAL MEMORY",
            initialKey = memory.key,
            initialFact = memory.fact,
            initialCategory = memory.category,
            onDismiss = { editingMemory = null },
            onConfirm = { key, fact, category ->
                viewModel.updateMemory(
                    memory.copy(
                        key = key,
                        fact = fact,
                        category = category
                    )
                )
                editingMemory = null
            }
        )
    }

    // Delete Confirmation Dialog
    deletingMemoryId?.let { id ->
        AlertDialog(
            onDismissRequest = { deletingMemoryId = null },
            containerColor = NexusCardDark,
            title = {
                Text(
                    text = "DELETE MEMORY?",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text(
                    text = "Nexus AI will erase this fact from its long-term neural memory. This action cannot be undone.",
                    color = NexusSilver,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMemory(id)
                        deletingMemoryId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { deletingMemoryId = null },
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                ) {
                    Text("Cancel", color = NexusSilverBright)
                }
            }
        )
    }
}

@Composable
private fun MemoryItemCard(
    memory: NexusMemory,
    onToggleActive: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryColor = when (memory.category.lowercase()) {
        "language" -> NeonEmerald
        "personal" -> NexusTitanium
        "preference" -> CyberViolet
        "work" -> NexusSilverBright
        else -> NexusSilver
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("memory_card_${memory.id}"),
        color = NexusCardDark,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (memory.isActive) NexusSilverDark else NexusBorderHairline
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Category Chip
                Surface(
                    color = NexusSurfaceDark,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                ) {
                    Text(
                        text = memory.category.uppercase(),
                        color = categoryColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (memory.isActive) "IN CONTEXT" else "DISABLED",
                        color = if (memory.isActive) NexusTitanium else NexusSilverDark,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(
                        checked = memory.isActive,
                        onCheckedChange = { onToggleActive() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NexusTitanium,
                            checkedTrackColor = NexusSurfaceElevated,
                            uncheckedThumbColor = NexusSilverDark,
                            uncheckedTrackColor = NexusDeepBlack
                        ),
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Key
            Text(
                text = memory.key,
                color = NexusSilverBright,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Fact Details
            Text(
                text = memory.fact,
                color = NexusSilver,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action row (Edit / Delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit memory",
                        tint = NexusTitanium,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete memory",
                        tint = NeonRed.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryEditorDialog(
    title: String,
    initialKey: String,
    initialFact: String,
    initialCategory: String,
    onDismiss: () -> Unit,
    onConfirm: (key: String, fact: String, category: String) -> Unit
) {
    var keyText by remember { mutableStateOf(initialKey) }
    var factText by remember { mutableStateOf(initialFact) }
    var categoryText by remember { mutableStateOf(initialCategory) }

    val categories = listOf("Personal", "Language", "Preference", "Work", "General")
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexusCardDark,
        title = {
            Text(
                text = title,
                color = NexusSilverBright,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Key / Label
                OutlinedTextField(
                    value = keyText,
                    onValueChange = { keyText = it },
                    label = { Text("Memory Label (e.g. My Name, Work, Tone)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexusSilverBright,
                        unfocusedTextColor = NexusSilverBright,
                        focusedBorderColor = NexusSilver,
                        unfocusedBorderColor = NexusBorderHairline
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = categoryText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    categoryText = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Fact details
                OutlinedTextField(
                    value = factText,
                    onValueChange = { factText = it },
                    label = { Text("Fact / Detail") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexusSilverBright,
                        unfocusedTextColor = NexusSilverBright,
                        focusedBorderColor = NexusSilver,
                        unfocusedBorderColor = NexusBorderHairline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (keyText.isNotBlank() && factText.isNotBlank()) {
                        onConfirm(keyText, factText, categoryText)
                    }
                },
                enabled = keyText.isNotBlank() && factText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NexusTitanium, contentColor = NexusDeepBlack)
            ) {
                Text("Save to Memory", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
            ) {
                Text("Cancel", color = NexusSilverBright)
            }
        }
    )
}

```

---

### File: `app/src/main/java/com/example/ui/screens/SettingsScreen.kt`

```kotlin
package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NexusBorderHairline
import com.example.ui.theme.NexusCardDark
import com.example.ui.theme.NexusDeepBlack
import com.example.ui.theme.NexusObsidian
import com.example.ui.theme.NexusPureBlack
import com.example.ui.theme.NexusSilver
import com.example.ui.theme.NexusSilverBright
import com.example.ui.theme.NexusSilverDark
import com.example.ui.theme.NexusSilverMuted
import com.example.ui.theme.NexusSurfaceDark
import com.example.ui.theme.NexusSurfaceElevated
import com.example.ui.theme.NexusTitanium
import com.example.viewmodel.ConnectionState
import com.example.viewmodel.NexusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onOpenMenu: () -> Unit = {}
) {
    val context = LocalContext.current
    val isApiKeyConfigured by viewModel.isApiKeyConfigured.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val languageMode by viewModel.languageMode.collectAsState()
    val personaStyle by viewModel.personaStyle.collectAsState()
    val isAutoSpeak by viewModel.isAutoSpeak.collectAsState()
    val ttsSpeed by viewModel.ttsSpeed.collectAsState()
    val ttsPitch by viewModel.ttsPitch.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

    var customKeyInput by remember { mutableStateOf("") }
    var isKeyVisible by remember { mutableStateOf(false) }

    val models = listOf("gemini-3.5-flash", "gemini-3.8-flash", "gemini-3.1-pro-preview")
    var modelDropdownExpanded by remember { mutableStateOf(false) }

    val languages = listOf("Bilingual (Auto)", "English Primary", "Hindi Primary (हिंदी)")
    var langDropdownExpanded by remember { mutableStateOf(false) }

    val personas = listOf("Futuristic AI", "Jarvis Companion", "Concise Professional", "Creative Thinker")
    var personaDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexusPureBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NexusSilverBright,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "SETTINGS",
                        color = NexusSilverBright,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "SYSTEM CONFIGURATION",
                        color = NexusSilver,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            IconButton(
                onClick = onOpenMenu,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = NexusSilver,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Section 1: API Key & Intelligence Provider
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isApiKeyConfigured) NexusBorderHairline else NeonRed.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = if (isApiKeyConfigured) NexusTitanium else NeonRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gemini API Connection",
                            color = NexusSilverBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val statusText = when (val conn = connectionState) {
                        is ConnectionState.Checking -> "CHECKING..."
                        is ConnectionState.NoKey -> "UNCONFIGURED"
                        is ConnectionState.Error -> "DISCONNECTED"
                        is ConnectionState.Online -> "VERIFIED ONLINE"
                    }
                    val statusColor = when (connectionState) {
                        is ConnectionState.Checking -> NeonAmber
                        is ConnectionState.NoKey -> NeonRed
                        is ConnectionState.Error -> NeonRed
                        is ConnectionState.Online -> NeonEmerald
                    }

                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = when (val conn = connectionState) {
                        is ConnectionState.Online -> "Verified connected to ${conn.model}. Official Gemini API integration is active."
                        is ConnectionState.Checking -> "Sending test telemetry ping to verify Gemini API connection..."
                        is ConnectionState.Error -> "Connection check failed: ${conn.message}"
                        is ConnectionState.NoKey -> "No API key detected. Set GEMINI_API_KEY in the Secrets panel in AI Studio or enter a key below."
                    },
                    color = NexusSilver,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = customKeyInput,
                    onValueChange = { customKeyInput = it },
                    label = { Text("Manual API Key Override (Optional)") },
                    placeholder = { Text("AIzaSy...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexusSilverBright,
                        unfocusedTextColor = NexusSilverBright,
                        focusedBorderColor = NexusSilver,
                        unfocusedBorderColor = NexusBorderHairline,
                        focusedContainerColor = NexusSurfaceDark,
                        unfocusedContainerColor = NexusSurfaceDark
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { isKeyVisible = !isKeyVisible },
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
                    ) {
                        Text(
                            text = if (isKeyVisible) "Hide Key" else "Reveal Key",
                            color = NexusSilver,
                            fontSize = 11.5.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (customKeyInput.isNotBlank()) {
                                viewModel.updateCustomApiKey(customKeyInput)
                                customKeyInput = ""
                                Toast.makeText(context, "API Key saved successfully", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NexusTitanium, contentColor = NexusDeepBlack),
                        enabled = customKeyInput.isNotBlank()
                    ) {
                        Text("Apply Key", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { viewModel.checkConnection() },
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTitanium)
                ) {
                    Text("Verify Neural Connection", color = NexusTitanium, fontSize = 12.sp)
                }
            }
        }

        // Section 2: AI Model Selection
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Model Engine",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select the Gemini neural model running Nexus AI.",
                    color = NexusSilver,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = modelDropdownExpanded,
                    onExpandedChange = { modelDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedModel,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline,
                            focusedContainerColor = NexusSurfaceDark,
                            unfocusedContainerColor = NexusSurfaceDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = modelDropdownExpanded,
                        onDismissRequest = { modelDropdownExpanded = false }
                    ) {
                        models.forEach { m ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(m, fontWeight = FontWeight.Bold)
                                        Text(
                                            when (m) {
                                                "gemini-3.5-flash" -> "Recommended • Fast, ultra-reliable multimodal chat"
                                                "gemini-3.8-flash" -> "Deep reasoning model with auto-fallback"
                                                else -> "Deep analytical thinking"
                                            },
                                            fontSize = 11.sp,
                                            color = NexusSilver
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.updateModel(m)
                                    modelDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Bilingual Support & Language Mode
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Language Mode (हिंदी / English)",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Nexus seamlessly understands and responds in both Hindi and English. Choose primary conversational bias.",
                    color = NexusSilver,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = langDropdownExpanded,
                    onExpandedChange = { langDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = languageMode,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = langDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline,
                            focusedContainerColor = NexusSurfaceDark,
                            unfocusedContainerColor = NexusSurfaceDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = langDropdownExpanded,
                        onDismissRequest = { langDropdownExpanded = false }
                    ) {
                        languages.forEach { l ->
                            DropdownMenuItem(
                                text = { Text(l) },
                                onClick = {
                                    viewModel.updateLanguageMode(l)
                                    langDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Section 4: Voice & Speech Engine (TTS)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = NexusTitanium,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auto-Read Responses",
                            color = NexusSilverBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Switch(
                        checked = isAutoSpeak,
                        onCheckedChange = { viewModel.updateAutoSpeak(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NexusTitanium,
                            checkedTrackColor = NexusSurfaceElevated,
                            uncheckedThumbColor = NexusSilverDark,
                            uncheckedTrackColor = NexusDeepBlack
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Speech Speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Speech Rate", color = NexusSilver, fontSize = 13.sp)
                    Text(String.format(java.util.Locale.US, "%.1fx", ttsSpeed), color = NexusTitanium, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = ttsSpeed,
                    onValueChange = { viewModel.updateTtsSettings(it, ttsPitch) },
                    valueRange = 0.6f..1.6f,
                    colors = SliderDefaults.colors(
                        thumbColor = NexusTitanium,
                        activeTrackColor = NexusSilver,
                        inactiveTrackColor = NexusSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Speech Pitch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Speech Pitch", color = NexusSilver, fontSize = 13.sp)
                    Text(String.format(java.util.Locale.US, "%.1fx", ttsPitch), color = NexusTitanium, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = ttsPitch,
                    onValueChange = { viewModel.updateTtsSettings(ttsSpeed, it) },
                    valueRange = 0.6f..1.4f,
                    colors = SliderDefaults.colors(
                        thumbColor = NexusTitanium,
                        activeTrackColor = NexusSilver,
                        inactiveTrackColor = NexusSurfaceElevated
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.ttsManager.speak(
                            text = "Hello! I am Nexus AI. नमस्ते, मैं नेक्सस एआई हूँ। How may I assist you today?",
                            speed = ttsSpeed,
                            pitch = ttsPitch
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusTitanium)
                ) {
                    Text("Test Bilingual Voice Output", color = NexusTitanium, fontSize = 13.sp)
                }
            }
        }

        // Section 5: AI Persona Style
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusCardDark,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = NexusTitanium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Persona Archetype",
                        color = NexusSilverBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = personaDropdownExpanded,
                    onExpandedChange = { personaDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = personaStyle,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = personaDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NexusSilverBright,
                            unfocusedTextColor = NexusSilverBright,
                            focusedBorderColor = NexusSilver,
                            unfocusedBorderColor = NexusBorderHairline,
                            focusedContainerColor = NexusSurfaceDark,
                            unfocusedContainerColor = NexusSurfaceDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = personaDropdownExpanded,
                        onDismissRequest = { personaDropdownExpanded = false }
                    ) {
                        personas.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p) },
                                onClick = {
                                    viewModel.updatePersonaStyle(p)
                                    personaDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // About Info
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NexusSurfaceDark,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexusBorderHairline)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NEXUS AI v1.0",
                    color = NexusSilverBright,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Futuristic Personal Intelligence • Built with Jetpack Compose & Gemini",
                    color = NexusSilverMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

```

---

### File: `app/src/test/java/com/example/GeminiApiIntegrationTest.kt`

```kotlin
package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.GeminiApiService
import com.example.data.api.GeminiCandidate
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiErrorResponse
import com.example.data.api.GeminiGenerateRequest
import com.example.data.api.GeminiGenerateResponse
import com.example.data.api.GeminiPart
import com.example.data.db.ChatDao
import com.example.data.db.ConversationDao
import com.example.data.db.MemoryDao
import com.example.data.db.NexusDatabase
import com.example.data.repository.NexusRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GeminiApiIntegrationTest {

    private lateinit var context: Context
    private lateinit var db: NexusDatabase
    private lateinit var chatDao: ChatDao
    private lateinit var convDao: ConversationDao
    private lateinit var memoryDao: MemoryDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = NexusDatabase.getInstance(context)
        chatDao = db.chatDao()
        convDao = db.conversationDao()
        memoryDao = db.memoryDao()
    }

    @Test
    fun testOfficialApiKeyConfiguration() {
        val repo = NexusRepository(context, chatDao, convDao, memoryDao)
        repo.setCustomApiKey("TEST_API_KEY_AIzaSy")

        assertEquals("TEST_API_KEY_AIzaSy", repo.getApiKey())
        assertTrue(repo.isApiKeyConfigured())
    }

    @Test
    fun testSuccessfulGeminiContentGeneration() = runBlocking {
        var usedApiKey = ""
        var usedModel = ""

        val fakeGeminiService = object : GeminiApiService {
            override suspend fun generateContent(
                model: String,
                apiKey: String,
                request: GeminiGenerateRequest
            ): GeminiGenerateResponse {
                usedApiKey = apiKey
                usedModel = model
                return GeminiGenerateResponse(
                    candidates = listOf(
                        GeminiCandidate(
                            content = GeminiContent(
                                role = "model",
                                parts = listOf(GeminiPart(text = "NEXUS online. Systems nominal."))
                            )
                        )
                    ),
                    error = null
                )
            }
        }

        val repo = NexusRepository(
            context = context,
            chatDao = chatDao,
            conversationDao = convDao,
            memoryDao = memoryDao,
            geminiService = fakeGeminiService
        )
        repo.setCustomApiKey("OFFICIAL_GEMINI_KEY")

        val convId = repo.createConversation("Test Session")
        val result = repo.askGemini(convId, "Greetings Nexus")

        assertTrue(result.isSuccess)
        assertEquals("NEXUS online. Systems nominal.", result.getOrNull())
        assertEquals("OFFICIAL_GEMINI_KEY", usedApiKey)
        assertEquals("gemini-3.5-flash", usedModel)
    }

    @Test
    fun testErrorSanitizationDoesNotExposeKey() = runBlocking {
        val fakeGeminiService = object : GeminiApiService {
            override suspend fun generateContent(
                model: String,
                apiKey: String,
                request: GeminiGenerateRequest
            ): GeminiGenerateResponse {
                return GeminiGenerateResponse(
                    candidates = null,
                    error = GeminiErrorResponse(
                        code = 400,
                        message = "API key AIzaSy1234567890abcdefghijklmn is invalid"
                    )
                )
            }
        }

        val repo = NexusRepository(
            context = context,
            chatDao = chatDao,
            conversationDao = convDao,
            memoryDao = memoryDao,
            geminiService = fakeGeminiService
        )
        repo.setCustomApiKey("AIzaSy1234567890abcdefghijklmn")

        val convId = repo.createConversation("Test Session")
        val result = repo.askGemini(convId, "Test key masking")

        assertTrue(result.isFailure)
        val errorMessage = result.exceptionOrNull()?.message ?: ""
        assertTrue(!errorMessage.contains("AIzaSy1234567890abcdefghijklmn"))
        assertTrue(errorMessage.contains("[REDACTED_API_KEY]"))
    }
}

```

---

### File: `app/src/test/java/com/example/ExampleRobolectricTest.kt`

```kotlin
package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NEXUS", appName)
  }
}

```

---

### File: `app/src/test/java/com/example/ExampleUnitTest.kt`

```kotlin
package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }
}

```

---

