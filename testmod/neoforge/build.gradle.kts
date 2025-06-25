@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
    `java-library`
}

val loader = prop("loom.platform")!!
val loaderCap = loader.upperCaseFirst()
val minecraft: String = stonecutter.current.version
val common: Project = requireNotNull(stonecutter.node.sibling("")) {
    "No common project for $project"
}.project

val ci = System.getenv("CI")?.toBoolean() ?: false
val release = System.getenv("RELEASE")?.toBoolean() ?: false
val nightly = ci && !release
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()
val testProject = project(":testmod")

version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraft}-${loader}${if (nightly) "-build.${buildNumber}" else ""}"
group = "${mod.group}.$loader"
base.archivesName = mod.id

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    decompilers {
        get("vineflower").apply {
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    silentMojangMappingsLicense()
    accessWidenerPath = common.loom.accessWidenerPath

    runConfigs {
        create("DataGenFabric") {
            data()
            name("Fabric Data Generation")
            programArgs("--all", "--mod", mod.id)
            programArgs("--output", "${project.rootProject.file("fabric/src/generated/resources")}")
            programArgs("--existing", "${project.rootProject.file("src/main/resources")}")
            vmArg("-Dtestmod.datagen.platform=fabric")
        }
        create("DataGenNeoForge") {
            data()
            name("NeoForge Data Generation")
            programArgs("--all", "--mod", mod.id)
            programArgs("--output", "${project.rootProject.file("neoforge/src/generated/resources")}")
            programArgs("--existing", "${project.rootProject.file("src/main/resources")}")
            vmArg("-Dtestmod.datagen.platform=neoforge")
        }
        all {
            isIdeConfigGenerated = true
            runDir = "../../../run"
            vmArgs("-Dmixin.debug.export=true")
        }
    }
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
    get("developmentNeoForge").extendsFrom(commonBundle)
}

dependencies {
    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    commonBundle(project(path = project.path.removePrefix(":testmod"), configuration = "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionNeoForge")) { isTransitive = false }

    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment("org.parchmentmc.data:parchment-$minecraft:${common.mod.dep("parchment_version")}@zip")
    })

    neoForge("net.neoforged:neoforge:${common.mod.dep("neoforge_loader")}")

    implementation(project(path = project.path.removePrefix(":testmod"), configuration = "namedElements")) { isTransitive = false }

    "io.github.llamalad7:mixinextras-neoforge:${mod.dep("mixin_extras")}".let {
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

tasks.jar { archiveClassifier = "dev" }

tasks.remapJar {
    injectAccessWidener = true
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
    properties(
        listOf("META-INF/neoforge.mods.toml", "pack.mcmeta"),
        "id" to mod.id,
        "name" to mod.name,
        "license" to mod.license,
        "version" to mod.version,
        "minecraft" to common.mod.prop("mc_dep_forgelike"),
        "authors" to mod.authors,
        "description" to mod.description
    )
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude("src/generated/resources/.cache")
        }
    }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

tasks.register("runDataGen") {
    group = "loom"
    description = "Generate data for ${mod.id}"
    dependsOn("runDataGenFabric", "runDataGenNeoForge")
}

tasks.build {
    dependsOn(tasks.remapJar)
}
