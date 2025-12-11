@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.*

plugins {
    `multiloader-loader`
    id("fabric-loom")
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

val visceraLibCorePathCommon: String = ":visceralib-core:common:${currentMod.mc}"
val visceraLibCorePathLoader: String = ":visceralib-core:fabric:${currentMod.mc}"

val main = sourceSets.getByName("main")
val testmod = sourceSets.create("testmod") {
    compileClasspath += main.compileClasspath
    runtimeClasspath += main.runtimeClasspath
}

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

val dependencyProjects: List<Project> = listOf(
    project(visceraLibCorePathCommon),
    project(visceraLibCorePathLoader)
)

dependencyProjects.forEach {
    project.evaluationDependsOn(it.path)
}

configurations.getByName("testmodImplementation").extendsFrom(configurations.getByName("implementation"))
configurations.getByName("testmodRuntimeClasspath").extendsFrom(configurations.getByName("runtimeClasspath"))

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

stonecutter {

}

dependencies {
    minecraft("com.mojang:minecraft:${currentMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        currentMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${currentMod.mc}:$parchmentVersion@zip")
        }
    })

    dependencyProjects.forEach {
        implementation(it)
    }

    modImplementation("net.fabricmc:fabric-loader:${currentMod.dep("fabric-loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${currentMod.dep("fabric-api")}+${currentMod.mc}")

    afterEvaluate {
        "testmodImplementation"(main.output)
        "testmodImplementation"(project(versionedCommonProjectPath).sourceSets.getByName("testmod").output)
    }
}

loom {
    accessWidenerPath = common.project.file("../../src/main/resources/accesswideners/${currentMod.mc}-visceralib_registration.accesswidener")

    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
        }
        create("testmodClient") {
            client()
            source(testmod)
            configName = "Testmod Fabric Client"
            ideConfigGenerated(true)
        }
    }

    mixin {
        defaultRefmapName = "visceralib_registration.refmap.json"
    }
}

tasks.named<ProcessResources>("processResources") {
    val awFile = project(commonProjectPath).file("src/main/resources/accesswideners/${currentMod.mc}-visceralib_registration.accesswidener")

    from(awFile.parentFile) {
        include(awFile.name)
        rename(awFile.name, "visceralib_registration.accesswidener")
        into("")
    }
}