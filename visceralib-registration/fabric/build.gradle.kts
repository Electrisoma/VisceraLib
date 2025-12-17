@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.*

plugins {
    `multiloader-loader`
    id("fabric-loom")
    id("dev.kikugie.fletching-table.fabric")
}

val main: SourceSet? = sourceSets.getByName("main")

val visceraLibCorePathCommon: String = ":visceralib-core:common:${currentMod.mc}"
val visceraLibCorePathLoader: String = ":visceralib-core:fabric:${currentMod.mc}"

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

val commonProject: List<Project> = listOf(
    project(visceraLibCorePathCommon)
)
val fabricProject: List<Project> = listOf(
    project(visceraLibCorePathLoader)
)
val dependencyProjects = commonProject + fabricProject

dependencyProjects.forEach {
    project.evaluationDependsOn(it.path)
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

dependencies {
    minecraft(
        group = "com.mojang",
        name = "minecraft",
        version = currentMod.mc
    )

    mappings(loom.layered {
        officialMojangMappings()
        currentMod.depOrNull("parchment")?.let {
            parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${currentMod.mc}:$parchmentVersion@zip")
        }
    })

    modImplementation(
        group = "net.fabricmc",
        name = "fabric-loader",
        version = currentMod.dep("fabric-loader")
    )

    modImplementation(
        group = "net.fabricmc.fabric-api",
        name = "fabric-api",
        version = "${currentMod.dep("fabric-api")}+${currentMod.mc}"
    )

    commonProject.forEach {
        implementation(it)
    }

    fabricProject.forEach {
        implementation(project(it.path, "namedElements"))
    }
}

loom {
    accessWidenerPath = common.project.file(
        "../../src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener"
    )

    val loomRunDir = File("../../../../run")

    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir(loomRunDir.resolve("client").toString())
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir(loomRunDir.resolve("server").toString())
        }
    }

    mixin {
        defaultRefmapName = "${currentMod.id}_${currentMod.module}.refmap.json"
    }
}

tasks.named<ProcessResources>("processResources") {
    val awFile = project(commonProjectPath).file(
        "src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener"
    )

    from(awFile.parentFile) {
        include(awFile.name)
        rename(awFile.name, "${currentMod.id}_${currentMod.module}.accesswidener")
        into("")
    }
}