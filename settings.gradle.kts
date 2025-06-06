pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.architectury.dev")
    }
}

plugins { id("dev.kikugie.stonecutter") version "0.6-beta.2" }

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    create(rootProject) { // Root `src/` functions as the 'common' project
        versions("1.21.1")
        branch("fabric") // Copies versions from root
        branch("neoforge") { versions("1.21.1") }
    }
}

rootProject.name = "ResoTech"
