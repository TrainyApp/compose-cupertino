@file:OptIn(
    ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class
)

import com.android.build.api.variant.impl.KotlinMultiplatformAndroidCompilationImpl
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.dokka.gradle.workers.ProcessIsolation
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import java.util.*

plugins {
    org.jetbrains.kotlin.multiplatform
    com.android.kotlin.multiplatform.library
    org.jetbrains.dokka
    id("com.vanniktech.maven.publish.base")
}

val _jvmTarget = findProperty("jvmTarget") as String

fun KotlinHierarchyBuilder.withAndroidLibrary(){
    withCompilations { it is KotlinMultiplatformAndroidCompilationImpl }
}

kotlin {
    applyDefaultHierarchyTemplate {
        common {
            group("skiko") {
                withApple()
                withJvm()
                withWasmJs()
                withJs()
            }
            group("nonIos") {
                withMacos()
                withJvm()
                withAndroidLibrary()
                withJs()
                withWasmJs()
            }

            group("darwin") {
                withApple()
            }

            group("jvm") {
                withAndroidLibrary()
                withJvm()
            }
        }
    }

    jvm("desktop")
    android {
        //noinspection WrongGradleMethod
        namespace = "io.github.alexzhirkevich.${name.filter { it.isLetter() }}"
        compileSdk = (findProperty("android.compileSdk") as String).toInt()

        minSdk { version = release((findProperty("android.minSdk") as String).toInt()) }

        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(_jvmTarget)
        }
    }

    targets.configureEach {
        if (this is HasConfigurableKotlinCompilerOptions<*>) {
            compilerOptions {
                if (this is KotlinJvmCompilerOptions) {
                    jvmTarget = JvmTarget.fromTarget(_jvmTarget)
                }
            }
        }
    }

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    iosArm64()
    iosSimulatorArm64()
    macosArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        optIn.add("kotlin.time.ExperimentalTime")
    }
}

dokka {
    // Dokka runs out of memory with the default maxHeapSize when ProcessIsolation is used
    (dokkaGeneratorIsolation.get() as? ProcessIsolation)?.maxHeapSize = "1g"

    dokkaSourceSets.configureEach {
        jdkVersion = 23 // Whilst we don't target 23, using 23 gives users a better Javadoc UI

        sourceLink {
            localDirectory = project.projectDir
            remoteUrl("https://github.com/alexzhirkevich/compose-cupertino/blob/master/${project.name}")
            remoteLineSuffix = "#L"
        }

        externalDocumentationLinks {
            register("kotlinx.coroutines") {
                url("https://kotlinlang.org/api/kotlinx.coroutines/")
            }
            register("kotlinx.serialization") {
                url("https://kotlinlang.org/api/kotlinx.serialization/")
            }
            register("kotlinx-datetime") {
                url("https://kotlinlang.org/api/kotlinx-datetime/")
                packageListUrl("https://kotlinlang.org/api/kotlinx-datetime/kotlinx-datetime/package-list")
            }
        }
    }
}

publishing {
    repositories {
        maven("https://europe-west3-maven.pkg.dev/mik-music/trainyapp") {
            credentials {
                username = "_json_key_base64"
                password = System.getenv("GOOGLE_KEY")?.toByteArray()?.let {
                    Base64.getEncoder().encodeToString(it)
                }
            }

            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "com.trainyapp.cupertino",
        artifactId = project.name,
        version = project.version.toString()
    )
    configure(
        KotlinMultiplatform(JavadocJar.Dokka("dokkaGeneratePublicationHtml"))
    )

    pom {
        name = project.name
        url = "https://github.com/alexzhirkevich/compose-cupertino"

        licenses {
            license {
                name = "Apache-2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }
        developers {
            developer {
                id = "alexzhirkevich"
                name = "Alexander Zhirkevich"
                email = "sasha.zhirkevich@gmail.com"
            }
        }
        scm {
            url = "https://github.com/alexzhirkevich/compose-cupertino"
            connection = "scm:git:git://github.com/alexzhirkevich/compose-cupertino.git"
            developerConnection = "scm:git:git://github.com/alexzhirkevich/compose-cupertino.git"
        }
    }
}
