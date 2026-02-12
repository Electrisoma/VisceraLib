pluginManagement {
    fun getProp(prop: String): String = providers.gradleProperty(prop).getOrElse("")

    plugins {
        id("dev.kikugie.fletching-table.fabric") version getProp("fletching_table")
        id("dev.kikugie.fletching-table.neoforge") version getProp("fletching_table")
        id("net.fabricmc.fabric-loom-remap") version getProp("loom")
        id("net.neoforged.moddev") version getProp("mdg")
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
    id("net.fabricmc.fabric-loom-remap") apply false
    id("net.neoforged.moddev") apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "visceralib"

fun module(name: String) {
    val loaders = providers.gradleProperty("supported_loaders").getOrElse("").split(",")
    val platforms = listOf("common") + loaders.map { it.trim() }.filter { it.isNotEmpty() }

    platforms.forEach {
        val projectPath = ":$name-$it"
        include(projectPath)
        project(projectPath).projectDir = file("$name/$it")

//        stonecutter {
//            kotlinController=true
//            centralScript="build.gradle.kts"
//            create(projectPath, Action<TreeBuilder> {
//                versions(*allVersions.toTypedArray())
//                vcsVersion="1.21.1"
//            })
//        }
    }
}

module("visceralib")
module("visceralib-core")
module("visceralib-datagen-api-v1")
module("visceralib-item-hooks-v1")
module("visceralib-registration-api-v1")
//module("visceralib-modelloader-api-v1") // empty atm
//module("visceralib-configs-api-v1") //temp
//module("visceralib-dsp-api-v1")     //temp
//module("visceralib-networking-api-v1") // networking
//module("visceralib-ui-api-v1")         // splashes

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
//        create(name, Action<TreeBuilder> {
//            versions(*allVersions.toTypedArray())
//            dists.forEach { (branch, versions) ->
//                branch(branch) { versions(*versions.toTypedArray()) }
//            }
//            vcsVersion="1.21.1"
//        })
//    }
//}