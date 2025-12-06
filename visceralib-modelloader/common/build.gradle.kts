@file:Suppress("UnstableApiUsage")

plugins {
    id("multiloader-common")
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

val visceraLibCorePathCommon: String = ":visceralib-core:common:${currentMod.mc}"

val main = sourceSets.getByName("main")
val testmod = sourceSets.create("testmod") {
    compileClasspath += main.compileClasspath
    runtimeClasspath += main.runtimeClasspath
}

configurations.getByName("testmodImplementation").extendsFrom(configurations.getByName("implementation"))
configurations.getByName("testmodRuntimeClasspath").extendsFrom(configurations.getByName("runtimeClasspath"))

loom {
    accessWidenerPath = common.project.file(
        "../../src/main/resources/accesswideners/${currentMod.mc}-visceralib_modelloader.accesswidener"
    )

    mixin {
        useLegacyMixinAp = false
    }
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

dependencies {
    implementation(project(visceraLibCorePathCommon))

    minecraft(group = "com.mojang", name = "minecraft", version = currentMod.mc)
    mappings(loom.layered {
        officialMojangMappings()
        currentMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${currentMod.mc}:$parchmentVersion@zip")
        }
    })

    compileOnly("org.spongepowered:mixin:0.8.5")

    "io.github.llamalad7:mixinextras-common:0.5.0".let {
        compileOnly(it)
        annotationProcessor(it)
    }

    modCompileOnly("net.fabricmc:fabric-loader:${currentMod.dep("fabric-loader")}")

    afterEvaluate {
        "testmodImplementation"(main.output)
    }
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    afterEvaluate {
        val mainSourceSet = main
        val testmodSourceSet = testmod

        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }

        testmodSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        testmodSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }
    }
}