import org.gradle.kotlin.dsl.*

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22" apply false
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22" apply false
    id("net.neoforged.moddev") version "2.0.115" apply false
    id("net.neoforged.moddev.legacyforge") version "2.0.107" apply false
    id("fabric-loom") version "1.11-SNAPSHOT" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "visceralib"

val commonVersions = providers.gradleProperty("stonecutter_enabled_common_versions")
    .orNull?.split(",")?.map { it.trim() } ?: emptyList()
val fabricVersions = providers.gradleProperty("stonecutter_enabled_fabric_versions")
    .orNull?.split(",")?.map { it.trim() } ?: emptyList()
val neoforgeVersions = providers.gradleProperty("stonecutter_enabled_neoforge_versions")
    .orNull?.split(",")?.map { it.trim() } ?: emptyList()

val dists = mapOf(
    "common" to commonVersions,
    "fabric" to fabricVersions,
    "neoforge" to neoforgeVersions
)
val uniqueVersions = dists.values.flatten().distinct()

fun module(name: String) {
    val projectName = ":$name"
    include(projectName)
    project(projectName).projectDir = file(name)

    stonecutter {
        kotlinController = true
        centralScript = "build.gradle.kts"

        create(project(projectName)) {
            versions(*uniqueVersions.toTypedArray())

            dists.forEach {
                (branchName, branchVersions) ->
                branch(branchName) {
                    versions(*branchVersions.toTypedArray())
                }
            }
        }
    }
}

module("visceralib-core")
//module("visceralib-datagen")
//module("visceralib-events")
//module("visceralib-modelloader")
//module("visceralib-registration")
