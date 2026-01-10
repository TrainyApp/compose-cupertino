@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    `library-module`
    alias(libs.plugins.serialization)
    alias(libs.plugins.composeJB)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        api(projects.cupertinoCore)
        implementation(libs.compose.runtime)
        implementation(libs.compose.foundation)
        implementation(libs.compose.uiutil)
        implementation(libs.datetime)
        implementation(libs.atomicfu)
        implementation(libs.serialization)
    }

}

mavenPublishing {
    pom {
        description =
            "Compose Multiplatform Cupertino theme and widgets based on Compose foundation"
    }
}
