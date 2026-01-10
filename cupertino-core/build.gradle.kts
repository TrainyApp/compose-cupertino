@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    `library-module`
    alias(libs.plugins.composeJB)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        implementation(libs.compose.runtime)
        implementation(libs.compose.foundation)
    }
}

mavenPublishing {
    pom {
        description = "Compose Cupertino shared module"
    }
}
