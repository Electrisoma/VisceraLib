import dev.kikugie.stonecutter.settings.tree.TreeBuilder

pluginManagement {
    fun getProp(prop: String): String = providers.gradleProperty(prop).getOrElse("")

    plugins {
        id("net.fabricmc.fabric-loom-remap") version getProp("loom")
        id("net.neoforged.moddev") version getProp("mdg")
        id("dev.kikugie.stonecutter") version getProp("stonecutter")
        id("dev.kikugie.fletching-table.fabric") version getProp("fletching_table")
        id("dev.kikugie.fletching-table.neoforge") version getProp("fletching_table")
    }

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
    id("dev.kikugie.fletching-table.fabric") apply false
    id("dev.kikugie.fletching-table.neoforge") apply false
    id("net.neoforged.moddev") apply false
    id("net.fabricmc.fabric-loom-remap") apply false
    id("dev.kikugie.stonecutter")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "visceralib"

fun getVersions(prop: String) = providers.gradleProperty(prop).getOrElse("")
    .split(",").map { it.trim() }.filter { it.isNotEmpty() }

val dists: Map<String, List<String>> = mapOf(
    "common"   to getVersions("stonecutter_enabled_common_versions"),
    "fabric"   to getVersions("stonecutter_enabled_fabric_versions"),
    "neoforge" to getVersions("stonecutter_enabled_neo_versions")
)

val allVersions = dists.values.flatten().distinct()

fun module(name: String) {
    include(name)

    stonecutter {
        kotlinController=true
        centralScript="build.gradle.kts"
        create(name, Action<TreeBuilder> {
            versions(*allVersions.toTypedArray())
            dists.forEach { (branch, versions) ->
                branch(branch) { versions(*versions.toTypedArray()) }
            }
            vcsVersion="1.21.1"
        })
    }
}

include("visceralib")

stonecutter {
    kotlinController=true
    centralScript="build.gradle.kts"
    create("visceralib", Action<TreeBuilder> {
        versions(*allVersions.toTypedArray())
        dists.forEach { (branch, versions) ->
            branch(branch) { versions(*versions.toTypedArray()) }
        }
        vcsVersion="1.21.1"
    })
}

module("visceralib-core")
//module("visceralib-modelloader-api-v1") // empty atm
module("visceralib-registration-api-v1")
//module("visceralib-datagen-api-v1") //temp
//module("visceralib-configs-api-v1") //temp
//module("visceralib-dsp-api-v1")     //temp
module("visceralib-item-hooks-v1")
//module("visceralib-networking-api-v1") // networking
//module("visceralib-ui-api-v1")         // splashes
