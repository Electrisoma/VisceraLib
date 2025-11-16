@file:Suppress("UnstableApiUsage")

import dev.ithundxr.silk.ChangelogText
import java.util.*

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.ithundxr.silk")
    `java-library`
}

val loader = prop("loom.platform")!!
val loaderCap = loader.upperCaseFirst()
val minecraft: String = stonecutter.current.version
val common: Project = requireNotNull(stonecutter.node.sibling("")) {
    "No common project for $project"
}.project

val ci = localProperties.getProperty("ci", "")?.toBoolean() ?: false
val release = localProperties.getProperty("release", "")?.toBoolean() ?: false
val nightly = ci && !release
val buildNumber = mod.build

version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraft}-${loader}${if (nightly) "-build.${buildNumber}" else ""}"
group = "${mod.group}.$loader"
base.archivesName = mod.id

architectury {
    platformSetupLoomIde()
    forge()
}

val commonBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}
val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}
configurations {
    compileClasspath.get().extendsFrom(commonBundle)
    runtimeClasspath.get().extendsFrom(commonBundle)
    get("developmentForge").extendsFrom(commonBundle)
}

sourceSets {
    main {
        resources { // include generated resources in resources
            srcDir("src/generated/resources")
            exclude("src/generated/resources/.cache")
        }
    }
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    silentMojangMappingsLicense()
    accessWidenerPath = common.loom.accessWidenerPath
    runConfigs {
        all {
            isIdeConfigGenerated = true
            runDir = "../../../run"
            vmArgs("-Dmixin.debug.export=true")
        }
    }
}

dependencies {
    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionForge")) { isTransitive = false }

    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment("org.parchmentmc.data:parchment-$minecraft:${common.mod.dep("parchment_version")}@zip")
    })

    "forge"("net.minecraftforge:forge:$minecraft-${common.mod.dep("forge_loader")}")

    "io.github.llamalad7:mixinextras-forge:${mod.dep("mixin_extras")}".let {
        implementation(it)
        include(it)
    }
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.remapJar)
            artifact(tasks.remapSourcesJar)
            group = mod.group
            artifactId = mod.id
        }
    }
}

tasks.jar { archiveClassifier = "dev" }
tasks.remapJar {
    injectAccessWidener.set(true)
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}
tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
    exclude("fabric.mod.json", "architectury.common.json")
}
tasks.processResources {
    val java = if (stonecutter.eval(minecraft, ">=1.20.5"))
        "JAVA_21" else "JAVA_17"

    properties(listOf("META-INF/mods.toml", "pack.mcmeta", "visceralib-forge.mixins.json"),
        "id" to mod.id, "name" to mod.name, "license" to mod.license,
        "version" to mod.version, "minecraft" to common.mod.prop("mc_dep_forgelike"),
        "authors" to mod.authors, "description" to mod.description,
        "compatibilityLevel" to java
    )
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

// Modmuss Publish
publishMods {
    file = tasks.remapJar.get().archiveFile
    changelog = ChangelogText.getChangelogText(rootProject).toString()
    displayName = "${common.mod.version} for $loaderCap $minecraft"
    modLoaders.add("forge")
    type = ALPHA

    curseforge {
        projectId = "publish.curseforge"
        accessToken = System.getenv("CURSEFORGE_TOKEN")
        minecraftVersions.add(minecraft)
    }
    modrinth {
        projectId = "publish.modrinth"
        accessToken = System.getenv("MODRINTH_TOKEN")
        minecraftVersions.add(minecraft)
    }

    dryRun = System.getenv("DRYRUN")?.toBoolean() ?: true
}