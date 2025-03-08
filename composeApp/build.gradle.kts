import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinxSerialization)
    // Cocoapods
    alias(libs.plugins.kotlinCocoapods)
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
            baseName = "composeApp"
            //      linkerOpts("-Xbinary=bundleId=com.iyr.ultrachangoWER3D825VB")
            isStatic = true
        }
    }


    // Cocoapods

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "12.0"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        //    name = "MyCocoaPod"

        podfile = project.file("../iosApp/Podfile")


        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "composeApp"
            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = true
        }
        /*
                pod("GoogleSignIn")

                pod("AppAuth")

                pod("FirebaseCore")
                pod("FirebaseAuth") {
                    // Add these lines
                    extraOpts += listOf("-compiler-option", "-fmodules")
                }

                pod("RecaptchaInterop") {
                    // Add these lines
                    extraOpts += listOf("-compiler-option", "-fmodules")
                }
        */
        // Dependencias de Firebase

        // Firebase Core Dependencies
        pod("FirebaseCore") {
            version = "~> 10.19.0"
        }
        pod("FirebaseAuth") {
            version = "~> 10.19.0"
        }

        // Authentication Providers
        pod("GoogleSignIn") {
            version = "~> 7.0"
        }

 //       pod("GoogleSignInSwift")
    }


//-----

    sourceSets {

        androidMain.dependencies {

            implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
            implementation("com.google.android.gms:play-services-auth:20.7.0")


            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.android.playservices.auth)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.splashScreen)
            implementation(libs.android.firebase.auth)
//            implementation(libs.firebase.core)

            // multimedia
            implementation(libs.androidx.media3.exoplayer)


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


            // QR
            implementation(libs.qr)

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
        }/*
        cInterop
                targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().all {
                    val mainCompilation = compilations.getByName("main")
                    // point our crashlytics.def file
                    mainCompilation.cinterops.create("firebaseauth") {
                        // Pass the header files location
                        includeDirs("$projectDir/src/include")
                        compilerOpts("-DNS_FORMAT_ARGUMENT(A)=", "-D_Nullable_result=_Nullable")
                        // Path to .def file
                        defFile("src/nativeInterop/cinterop/firebaseauth.def")
                        compilerOpts("-framework", "MyFramework", "-F/Users/user/Projects/MyFramework/ios/SDK")
                    }
                }
        */

    }

    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
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
        val webClientId = "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$webClientId\"")
    }

    // Añadir esta configuración
    buildFeatures {
        buildConfig = true
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


dependencies {
// add("kspCommonMainMetadata", libs.androidx.room.compiler)


}


dependencies {
    implementation(libs.play.services.wallet)
    implementation(libs.androidx.sqlite.ktx)
    implementation(libs.androidx.media3.exoplayer)
    // implementation(libs.firebase.auth)
    debugImplementation(compose.uiTooling)
}


