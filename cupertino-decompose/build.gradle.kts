@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    `library-module`
    alias(libs.plugins.composeJB)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        api(projects.cupertinoCore)
        implementation(libs.compose.runtime)
        implementation(libs.compose.ui)
        implementation(libs.compose.animation)
        implementation(libs.decompose.compose)
        implementation(libs.decompose.core)
    }
}

mavenPublishing {
    pom {
        description = "Cupertino extensions for Decompose library"
    }
}
