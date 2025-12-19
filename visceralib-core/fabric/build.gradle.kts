@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.*
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    `multiloader-loader`
    id("fabric-loom")
    id("maven-publish")
    id("dev.kikugie.fletching-table.fabric")
}

val main: SourceSet? = sourceSets.getByName("main")

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

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
            runDir(loomRunDir.resolve("client").toString())
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            runDir(loomRunDir.resolve("server").toString())
        }
        all {
            ideConfigGenerated(true)
        }
    }

    mixin {
        defaultRefmapName = "${currentMod.id}_${currentMod.module}.refmap.json"
    }
}

tasks.named<ProcessResources>("processResources") {
    val commonResDir = common.project.file("src/main/resources")
    val awFile = commonResDir.resolve("accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener")

    if (awFile.exists()) {
        from(awFile) {
            rename(awFile.name, "${currentMod.id}_${currentMod.module}.accesswidener")
            into("")
        }
    }
}
apply(plugin = "maven-publish")

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            if (project.plugins.hasPlugin("fabric-loom")) {
                artifact(tasks.named("remapJar"))
            } else {
                artifact(tasks.named("jar"))
            }

            artifact(tasks.named("sourcesJar"))

            artifactId = "${currentMod.id}-${currentMod.module}-$loader-${currentMod.mc}"
            group = currentMod.group
            version = "${currentMod.version}+mc${currentMod.mc}"
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/electrisoma/VisceraLib")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: localProperties.getProperty("mavenUsername", "")
                password = System.getenv("GITHUB_TOKEN") ?: localProperties.getProperty("mavenToken", "")
            }
        }
        mavenLocal()
    }
}