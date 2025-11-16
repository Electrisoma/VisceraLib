@file:Suppress("UnstableApiUsage")

import java.util.*

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    `maven-publish`
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.ithundxr.silk")
    `java-library`
}

val minecraft = stonecutter.current.version

val ci = localProperties.getProperty("ci", "")?.toBoolean() ?: false
val release = localProperties.getProperty("release", "")?.toBoolean() ?: false
val nightly = ci && !release
val buildNumber = mod.build

version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraft}-common${if (nightly) "-build.${buildNumber}" else ""}"
group = "${group}.common"
base.archivesName.set(mod.id)

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.project.prop("loom.platform")
})

loom {
    decompilers {
        get("vineflower").apply {
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    silentMojangMappingsLicense()
    accessWidenerPath = rootProject.file("src/main/resources/${mod.id}.accesswidener")
    runConfigs.all {
        isIdeConfigGenerated = true
        runDir = "../../../run"
        vmArgs("-Dmixin.debug.export=true")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment("org.parchmentmc.data:parchment-$minecraft:${mod.dep("parchment_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api_version")}")

    "io.github.llamalad7:mixinextras-common:${mod.dep("mixin_extras")}".let {
        annotationProcessor(it)
        implementation(it)
    }
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.processResources {
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        "JAVA_21" else "JAVA_17"

    properties(listOf("visceralib-common.mixins.json"),
        "compatibilityLevel" to java
    )
}

tasks.register("${mod.id}Publish") {
    when (val platform = System.getenv("PLATFORM")) {
        "all" -> {
            dependsOn(tasks.build,
                ":fabric:publish", ":forge:publish", ":neoforge:publish", ":common:publish",
                ":fabric:publishMods", ":forge:publishMods", ":neoforge:publishMods")
        }
        "fabric", "forge", "neoforge" -> {
            dependsOn("${platform}:build", "${platform}:publish", "${platform}:publishMods")
        }
    }
}