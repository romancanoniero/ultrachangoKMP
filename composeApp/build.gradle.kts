import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import java.util.Properties

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

// Cargar el archivo secrets.properties
val secrets = Properties().apply {
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        load(secretsFile.inputStream())
    }
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
            freeCompilerArgs += listOf("-Xbinary=bundleId=com.iyr.ultrachango")
            isStatic = true
        }
    }


    // Cocoapods

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "14.0"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
            name = "UltraChangoPod"

        podfile = project.file("../iosApp/Podfile")


        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "composeApp"
            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = true
           binaryOption("bundleId", "com.iyr.ultrachango.composeApp")
        }

        pod("FirebaseAuth") {
            version = "~> 10.0"
        }
        // Firebase Core Dependencies
        pod("FirebaseCore") {
            version = "~> 10.19.0"
        }

        pod("GoogleSignIn") {
            version = "~> 7.0.0"
        }


        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }


//-----

    sourceSets {

        androidMain.dependencies {

            implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
            implementation("com.google.android.gms:play-services-auth:20.7.0")


            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.core.google.shortcuts)
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

            implementation("de.drick.compose:hotpreview:0.1.4")

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


       //     implementation(libs.remember.settings)
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

            // Authentication
            implementation("com.iyr.fbauthentication:firebase-auth-kmp:1.0.32")


            // For FilePicker
            implementation(libs.calf.file.picker)
            // For FilePicker
            implementation(libs.calf.file.picker.coil)

            implementation(libs.kim)

            // Auth
  /*
            implementation(libs.kmpaut.google)
            implementation(libs.kmpaut.uihelper)
            implementation(libs.kmpaut.firebase)
*/
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation("io.insert-koin:koin-core:3.5.3")
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
            implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
            implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.21")

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        // Testing Dependencies
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.koin.test)
        }

        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.robolectric)
            implementation(libs.androidx.test.junit)
            implementation(libs.androidx.espresso.core)
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.junit)
            implementation(libs.androidx.espresso.core)
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }

        iosTest.dependencies {
            implementation(libs.kotlin.test)
        }

        /*
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
      /*
        val webClientId =
            "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$webClientId\"")
*/

        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"${secrets["GOOGLE_WEB_CLIENT_ID"]}\"")
        buildConfigField("String", "FACEBOOK_APP_ID", "\"${secrets["FACEBOOK_APP_ID"]}\"")
        buildConfigField("String", "FACEBOOK_CLIENT_TOKEN", "\"${secrets["FACEBOOK_CLIENT_TOKEN"]}\"")


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
    implementation(libs.androidx.ui.text.android)
    // implementation(libs.firebase.auth)
    debugImplementation(compose.uiTooling)
}


