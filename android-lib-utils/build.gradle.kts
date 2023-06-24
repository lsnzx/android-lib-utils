plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android {
    namespace = "com.lsn.lib.utils"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.lsn.lib"
                artifactId = "utils"
                version = "1.2.2"
//                from(components["java"])
                afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            }
        }
    }

}



dependencies {

//    implementation("com.google.code.gson:gson:2.9.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
//
//    implementation("com.google.android.material:material:1.8.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.activity:activity-ktx:1.6.1")
//    implementation("androidx.fragment:fragment-ktx:1.5.5")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.recyclerview:recyclerview:1.2.0")
//    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")

    implementation(libs.gson)
    implementation(libs.okhttp3.logging.interceptor)

    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.constraintlayout.constraintlayout)
    implementation(libs.androidx.recyclerview.recyclerview)
    implementation(libs.jetbrains.kotlin.reflect)
    implementation(files("libs\\pinyin4j-2.5.0.jar"))
}