@file:Suppress("UnstableApiUsage")

plugins {
    id("java")
    id("java-library")
}

version = "${loader}-${currentMod.version}+mc${stonecutterBuild.current.version}"

base {
    archivesName = currentMod.id
}

java {
    toolchain.languageVersion.set(project.providers.provider {
        JavaLanguageVersion.of(currentMod.prop("java.version").toInt())
    })

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
    maven("https://maven.kikugie.dev/releases")
    maven("https://maven.kikugie.dev/snapshots")

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
            maven("https://maven.parchmentmc.org"),
            maven("https://maven.neoforged.net/releases"),
            maven("https://maven.minecraftforge.net/")
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
            "java"               to currentMod.propOrNull("java.version"),
            "compatibilityLevel" to currentMod.propOrNull("java.version")?.let { "JAVA_$it" },
            "id"                 to currentMod.id,
            "name"               to currentMod.name,
            "version"            to currentMod.version,
            "group"              to currentMod.group,
            "authors"            to currentMod.author,
            "description"        to currentMod.description,
            "license"            to currentMod.license,
            "github"             to currentMod.github,
            "minecraft"          to currentMod.propOrNull("minecraft_version"),
            "minMinecraft"       to currentMod.propOrNull("min_minecraft_version"),
            "fabric"             to currentMod.depOrNull("fabric_loader"),
            "FApi"               to currentMod.depOrNull("fabric_api"),
            "neoForge"           to currentMod.depOrNull("neoforge"),
            "forge"              to currentMod.depOrNull("forge")
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
    dependsOn("${project.parent!!.path}:${currentMod.mc}:stonecutterGenerate")
}
