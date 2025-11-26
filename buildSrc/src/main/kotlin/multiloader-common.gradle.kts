@file:Suppress("UnstableApiUsage")

plugins {
    id("java")
    id("java-library")
}

version = "${loader}-${commonMod.version}+mc${stonecutterBuild.current.version}"

base {
    archivesName = commonMod.id
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(commonProject.prop("java.version")!!)

    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://www.cursemaven.com")
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.kikugie.dev/releases") {
        name = "KikuGie Releases"
    }
    maven("https://maven.kikugie.dev/snapshots") {
        name = "KikuGie Snapshots"
    }

    exclusiveContent {
        forRepository {
            maven("https://repo.spongepowered.org/repository/maven-public") {
                name = "Sponge"
            }
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }
    exclusiveContent {
        forRepositories(
            maven("https://maven.parchmentmc.org") {
                name = "ParchmentMC"
            },
            maven("https://maven.neoforged.net/releases") {
                name = "NeoForge"
            },
            maven("https://maven.minecraftforge.net/") {
                name = "MinecraftForge"
            }
        )
        filter { includeGroup("org.parchmentmc.data") }
    }
}

dependencies {
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
}

tasks {

    processResources {
        val expandProps = mapOf(
            "java" to commonMod.propOrNull("java.version"),
            "compatibilityLevel" to commonMod.propOrNull("java.version")?.let { "JAVA_$it" },
            "id" to commonMod.id,
            "name" to commonMod.name,
            "version" to commonMod.version,
            "group" to commonMod.group,
            "authors" to commonMod.author,
            "description" to commonMod.description,
            "license" to commonMod.license,
            "github" to commonMod.github,
            "minecraft" to commonMod.propOrNull("minecraft_version"),
            "minMinecraft" to commonMod.propOrNull("min_minecraft_version"),
            "fabric" to commonMod.depOrNull("fabric_loader"),
            "FApi" to commonMod.depOrNull("fabric_api"),
            "neoForge" to commonMod.depOrNull("neoforge"),
            "forge" to commonMod.depOrNull("forge")
        ).filterValues { it?.isNotEmpty() == true }.mapValues { (_, v) -> v!! }

        val jsonExpandProps = expandProps.mapValues { (_, v) -> v.replace("\n", "\\\\n") }

        filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }
}

tasks.named("processResources") {
    dependsOn("${project.parent!!.path}:${commonMod.mc}:stonecutterGenerate")
}
