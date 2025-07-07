import com.vanniktech.maven.publish.SonatypeHost
import java.util.Base64


val VERSION_NAME = project.findProperty("versionName") as? String ?: "undefined"

plugins {
    id("maven-publish")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.vanniktech.maven.publish)

}

android {
    namespace = "dev.piotrprus.composeparticleemitter"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(
        groupId = "dev.piotrprus",
        artifactId = "compose-particle-emitter",
        version = VERSION_NAME
    )

    pom {
        name = "Compose Particle Emitter"
        description = "A lightweight library to play with canvas particle emitter"
        inceptionYear = "2025"
        url = "https://github.com/PiotrPrus/ComposeParticleEmitter"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "PiotrPrus"
                name = "Piotr Prus"
                url = "https://github.com/PiotrPrus"
            }
        }
        scm {
            url = "https://github.com/PiotrPrus/ComposeParticleEmitter"
            connection = "scm:git:git://github.com/PiotrPrus/ComposeParticleEmitter.git"
            developerConnection = "scm:git:ssh://github.com/PiotrPrus/ComposeParticleEmitter.git"
        }
    }
}



afterEvaluate {
    extensions.findByType(org.gradle.plugins.signing.SigningExtension::class.java)?.apply {
        val key = System.getenv("SIGNING_KEY")
        val password = System.getenv("SIGNING_PASSWORD")

        if (!key.isNullOrBlank() && !password.isNullOrBlank()) {
            val decodedKey = try {
                String(Base64.getDecoder().decode(key))
            } catch (e: IllegalArgumentException) {
                key
            }

            useInMemoryPgpKeys(decodedKey, password)
        }
    }
}

