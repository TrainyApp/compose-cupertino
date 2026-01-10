@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    `library-module`
    alias(libs.plugins.composeJB)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        api(projects.cupertino)
        api(projects.cupertinoNative)
        implementation(projects.cupertinoCore)
        api(libs.compose.material3)
        implementation(libs.compose.runtime)
        implementation(libs.compose.foundation)
        implementation(libs.compose.material.icons.extended)
        implementation(libs.compose.uiutil)
    }

    compilerOptions {
        optIn.addAll(
            "io.github.alexzhirkevich.cupertino.ExperimentalCupertinoApi",
            "io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi"
        )
    }
}

mavenPublishing {
    pom {
        description = "Compose Multiplatform adaptive theme and wrappers for Cupertino and Material 3 widgets"
    }
}
