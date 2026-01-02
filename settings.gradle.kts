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
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22" apply false
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22" apply false
    id("net.neoforged.moddev") version "2.0.134" apply false
    id("fabric-loom") version "1.12-SNAPSHOT" apply false
    id("dev.kikugie.stonecutter") version "0.7.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "visceralib"

fun getVersions(prop: String) = providers.gradleProperty(prop).getOrElse("")
    .split(",").map { it.trim() }.filter { it.isNotEmpty() }

val dists = mapOf(
    "common"   to getVersions("stonecutter_enabled_common_versions"),
    "fabric"   to getVersions("stonecutter_enabled_fabric_versions"),
    "neoforge" to getVersions("stonecutter_enabled_neo_versions")
)

val allVersions = dists.values.flatten().distinct()

fun module(name: String) {
    include(name)

    stonecutter {
        kotlinController = true
        centralScript = "build.gradle.kts"

        create(project(":$name")) {
            versions(*allVersions.toTypedArray())
            dists.forEach { (branch, versions) ->
                branch(branch) { versions(*versions.toTypedArray()) }
            }
        }
    }
}

// TODO: networking, item hooks(?)
module("visceralib-core")
module("visceralib-configs-api")
module("visceralib-datagen-api")
module("visceralib-modelloader-api")
module("visceralib-registration-api")
module("visceralib-dsp-api")