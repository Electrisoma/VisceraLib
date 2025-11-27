@file:Suppress("UnstableApiUsage")

plugins {
    id("multiloader-common")
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

sourceSets {
    create("testmod") {
        val mainOutput = project.sourceSets.getByName("main").output
        compileClasspath += mainOutput
        runtimeClasspath += mainOutput

        resources {
            srcDir("src/testmod/resources")
        }
        java {
            srcDir("src/testmod/java")
        }
    }
}

loom {
    accessWidenerPath = common.project.file(
        "../../src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}.accesswidener"
    )

    mixin {
        useLegacyMixinAp = false
    }
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
    j52j.register("testmod") {
        extension("json", "**/*.json5")
    }
}

dependencies {
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
}


val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val testmodJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val testmodResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    afterEvaluate {
        val mainSourceSet = sourceSets.main.get()

        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }

        val testmodSourceSet = sourceSets["testmod"]

        testmodSourceSet.java.sourceDirectories.files.forEach {
            add(testmodJava.name, it)
        }
        testmodSourceSet.resources.sourceDirectories.files.forEach {
            add(testmodResources.name, it)
        }
    }
}