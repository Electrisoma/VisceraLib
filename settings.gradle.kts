pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "visceralib"

fun module(name: String) {
    val loaders = providers.gradleProperty("supported_loaders").getOrElse("").split(",")
    val platforms = listOf("common") + loaders.map { it.trim() }.filter { it.isNotEmpty() }

    platforms.forEach {
        include(":$name-$it")
        project(":$name-$it").projectDir = file("$name/$it")
    }
}

module("visceralib")                        // bundle of all modules into 1 project transitively, meant for publication
//module("visceralib-configs-api-v1")         // config screen helper
module("visceralib-core")                   // basic infrastructure for all modules
module("visceralib-datagen-api-v1")         // platform-agnostic data providers
module("visceralib-dsp-api-v1")             // event-driven digital sound processing effects
module("visceralib-item-hooks-v1")          // platform-agnostic interface injection based item hooks
//module("visceralib-model-loading-api-v1")   // custom model loading helpers
//module("visceralib-networking-api-v1")      // packet distribution and creation
module("visceralib-registration-api-v1")    // deferred registration system
module("visceralib-rendering-api-v1")       // rendering helpers and events
module("visceralib-splashes-v1")            // custom resource pack and event-driven splash texts
//module("visceralib-ui-api-v1")              // ui modification and screen creation, might merge splashes into this