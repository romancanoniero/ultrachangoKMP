import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.ksp)
   //  alias(libs.plugins.androidxRoom)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            freeCompilerArgs += listOf("-Xbinary=bundleId=com.iyr.ultrachango")
            isStatic = true
        }
    }

    /*
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }
*/

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.android.playservices.auth)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.splashScreen)
/*
            implementation("com.google.android.gms:play-services-assistant:1.0.0")
            implementation("com.google.android.gms:play-services-voice:1.0.0")
      */
        }
        commonMain.dependencies {
            api(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Preferences
            implementation(libs.settings)


            // Navegacion
            implementation(libs.navigation.compose)

            // window-size
            implementation(libs.screen.size)


            // Permisos
            implementation(libs.permissions.moko)

            // Imagenes
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // Authentication
            implementation(libs.gitlive.firebase.common)
            implementation(libs.gitlive.firebase.auth)

            implementation(libs.kmpaut.google)
            implementation(libs.kmpaut.firebase)
            implementation(libs.kmpaut.uihelper)

            // HttpClient
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // Room
            //implementation(libs.androidx.room.runtime)
            //implementation(libs.androidx.sqlite.bundled)

            // Injeccion de Dependencias
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Componenetes
            implementation(libs.swipebox) // Libreria de swipereveal en las listas
            implementation(libs.pullrefresh) // Libreria de swipereveal en las listas


            // Barcode scanner
            implementation(libs.barcodescanning)

            // multimedia
            implementation(libs.androidx.media3.exoplayer)


            // Places
            implementation(libs.placesautocomplete)

            // Geocoding
            // Geolocation
            implementation(libs.compass.geolocation)
            implementation(libs.compass.geolocation.mobile)

            // Geocoding
            implementation(libs.compass.geocoder)
            implementation(libs.compass.geocoder.mobile)

            // Autocomplete
            implementation(libs.compass.autocomplete)
            implementation(libs.compass.autocomplete.mobile)

            // Location permissions for mobile
            implementation(libs.compass.permissions.mobile)

            // Wheel date picker
            //   implementation(libs.)
            implementation(libs.kotlinx.datetime)
            implementation(libs.datetime.wheel.picker)

            // peekaboo
  /*
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)

*/
            // For FilePicker
            implementation(libs.calf.file.picker)
            // For FilePicker
            implementation(libs.calf.file.picker.coil)

            implementation(libs.kim)

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.iyr.ultrachango"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.iyr.ultrachango"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

/*
ksp {
    // arg("kapt.use.worker.api", "false")
    arg("room.schemaLocation", "${projectDir}/schemas")
}
*/
dependencies {
   // add("kspCommonMainMetadata", libs.androidx.room.compiler)



}

/*
dependencies {
    listOf(
        "kspAndroid",
        // "kspJvm",
        "kspIosSimulatorArm64", "kspIosX64", "kspIosArm64"
    ).forEach {
        add(it, libs.androidx.room.compiler)
    }
}
*/



/*
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

*/


dependencies {
    implementation(libs.play.services.wallet)
    implementation(libs.androidx.sqlite.ktx)
    implementation(libs.androidx.media3.exoplayer)
    debugImplementation(compose.uiTooling)
}


