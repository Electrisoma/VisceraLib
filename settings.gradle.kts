pluginManagement {
    fun getProp(prop: String): String? = providers.gradleProperty(prop).getOrElse("")

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

include("visceralib-core")

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(project(":visceralib-core")) {
        versions("1.21.1")
        branch("common")
        branch("fabric")
        branch("neoforge")
    }
}

//fun getVersions(prop: String) = providers.gradleProperty(prop).getOrElse("")
//    .split(",").map { it.trim() }.filter { it.isNotEmpty() }
//
//val dists: Map<String, List<String>> = mapOf(
//    "common"   to getVersions("stonecutter_enabled_common_versions"),
//    "fabric"   to getVersions("stonecutter_enabled_fabric_versions"),
//    "neoforge" to getVersions("stonecutter_enabled_neo_versions")
//)
//
//val allVersions = dists.values.flatten().distinct()

//fun module(name: String) {
//    include(name)
//
//    stonecutter {
//        kotlinController=true
//        centralScript="build.gradle.kts"
//
//        create(project(":$name")) {
//            versions(*allVersions.toTypedArray())
//            dists.forEach { (branch, versions) ->
//                branch(branch) { versions(*versions.toTypedArray()) }
//            }
//            vcsVersion = "1.21.1"
//        }
//    }
//}

// TODO: networking, item hooks(?)
//module("visceralib-core")
//module("visceralib-modelloader-api") // empty atm
//module("visceralib-registration-api")
//module("visceralib-datagen-api")
//module("visceralib-configs-api")
//module("visceralib-dsp-api")
//module("visceralib-item-hooks-api") // item stuff
//module("visceralib-networking-api") // networking
//module("visceralib-ui-api")         // splashes
