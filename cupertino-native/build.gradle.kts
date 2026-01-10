plugins {
    `library-module`
    alias(libs.plugins.composeJB)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.cupertinoCore)
            implementation(projects.cupertino)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
        }
    }
}

mavenPublishing {
    pom {
        description = "UIKit native wrappers for Compose Multiplatform Cupertino Widgets"
    }
}
