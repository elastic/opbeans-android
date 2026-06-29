import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.firebase.appDistribution)
    id("co.elastic.otel.android.agent")
    id("co.elastic.otel.android.instrumentation.okhttp")
}

android {
    compileSdk = 36
    namespace = "co.elastic.apm.opbeans"
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "co.elastic.apm.opbeans"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "OPBEANS_URL",
            "\"${project.properties.getOrDefault("opbeans_endpoint", "http://10.0.2.2:3000")}\""
        )
        buildConfigField(
            "String",
            "OPBEANS_AUTH_TOKEN",
            "\"${project.properties.getOrDefault("opbeans_auth_token", "")}\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.get("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            firebaseAppDistribution {
                appId = System.getenv("OPBEANS_APP_ID")
                artifactType = "APK"
                groups = "elastic-testers"
                releaseNotes =
                    project.properties.getOrDefault("firebase_release_notes", "None") as String
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        animationsDisabled = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

// Hilt's bundled kotlin-metadata-jvm (2.2.20) only supports up to metadata 2.3.0.
// Force 2.4.0 until Hilt ships a release that adopts it natively.
configurations.configureEach {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-metadata-jvm:2.4.0")
    }
}

val espresso_version = "3.5.1"
dependencies {
    implementation(libs.android.material)
    implementation(libs.android.fragment)
    implementation(libs.hilt)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.glide)
    implementation(libs.glide.okhttp)
    implementation(libs.android.room)
    implementation(libs.android.room.ktx)
    implementation(libs.android.swiperefresh)
    implementation(libs.android.paging)
    ksp(libs.android.room.compiler)
    ksp(libs.glide.compiler)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_version")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espresso_version")
}

spotless {
    java {
        target("src/*/java/**/*.java")
        licenseHeader(getSourceHeader())
    }
    kotlin {
        target("src/*/java/**/*.kt")
        licenseHeader(getSourceHeader())
    }
}

fun getSourceHeader(): String {
    return """
        /* 
        Licensed to Elasticsearch B.V. under one or more contributor
        license agreements. See the NOTICE file distributed with
        this work for additional information regarding copyright
        ownership. Elasticsearch B.V. licenses this file to you under
        the Apache License, Version 2.0 (the "License"); you may
        not use this file except in compliance with the License.
        You may obtain a copy of the License at
        
          http://www.apache.org/licenses/LICENSE-2.0
        
        Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
        KIND, either express or implied.  See the License for the
        specific language governing permissions and limitations
        under the License. 
        */
    """.trimIndent()
}
