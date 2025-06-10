@file:Suppress("UnstableApiUsage")

import dev.ithundxr.silk.ChangelogText

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.ithundxr.silk")
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

version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraft}-${loader}${if (nightly) "-build.${buildNumber}" else ""}"
group = "${mod.group}.$loader"
base.archivesName.set(mod.id)

architectury {
    platformSetupLoomIde()
    fabric()
}

val portLibModules = listOf(
    "tags", "models", "data", "lazy_registration"
)

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
    get("developmentFabric").extendsFrom(commonBundle)
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
//        create("DataGenFabric") {
//            client()
//
//            name("Fabric Data Generation")
//            vmArg("-Dfabric-api.datagen")
//            vmArg("-Dfabric-api.datagen.output-dir=${project.rootProject.file("fabric/src/generated/resources")}")
//            vmArg("-Dfabric-api.datagen.modid=resotech")
//            vmArg("-Dporting_lib.datagen.existing_resources=${project.rootProject.file("common/src/main/resources")}")
//            vmArg("-Dresotech.datagen.platform=fabric")
//        }
//        create("DataGenNeoForge") {
//            client()
//
//            name("NeoForge Data Generation (Fabric)")
//            vmArg("-Dfabric-api.datagen")
//            vmArg("-Dfabric-api.datagen.output-dir=${project.rootProject.file("neoforge/src/generated/resources")}")
//            vmArg("-Dfabric-api.datagen.modid=resotech")
//            vmArg("-Dporting_lib.datagen.existing_resources=${project.rootProject.file("common/src/main/resources")}")
//            vmArg("-Dresotech.datagen.platform=neoforge")
//        }
        all {
            isIdeConfigGenerated = true
            runDir = "../../../run"
            //vmArgs("-Dmixin.debug.export=true")
        }
    }
}

dependencies {
    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionFabric")) { isTransitive = false }

    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment("org.parchmentmc.data:parchment-$minecraft:${common.mod.dep("parchment_version")}@zip")
    })

    modApi("net.fabricmc.fabric-api:fabric-api:${common.mod.dep("fabric_api_version")}")
    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")

    for (module in portLibModules) {
        modApi(include("io.github.fabricators_of_create.Porting-Lib:$module:${common.mod.dep("portingLib")}")!!)
    }

    "dev.architectury:architectury-fabric:${common.mod.dep("archApi")}".let {
        modImplementation(it)
        include(it)
    }

    "net.createmod.ponder:Ponder-Fabric-$minecraft:${common.mod.dep("ponder")}".let {
        modImplementation(it)
        include(it)
    }
    compileOnly("dev.engine-room.flywheel:flywheel-fabric-api-$minecraft:${common.mod.dep("flywheel")}")
    "dev.engine-room.flywheel:flywheel-fabric-$minecraft:${common.mod.dep("flywheel")}".let {
        modImplementation(it)
        include(it)
    }
    "foundry.veil:veil-fabric-$minecraft:${common.mod.dep("veil")}".let {
        modImplementation(it)
        include(it)
    }

    modRuntimeOnly("maven.modrinth:modmenu:${common.mod.dep("modmenu")}")

    "io.github.llamalad7:mixinextras-fabric:${mod.dep("mixin_extras")}".let {
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

tasks.shadowJar {
    configurations = listOf(shadowBundle)
    archiveClassifier = "dev-shadow"
}

tasks.remapJar {
    injectAccessWidener = true
    input = tasks.shadowJar.get().archiveFile
    archiveClassifier = null
    dependsOn(tasks.shadowJar)
}

tasks.jar { archiveClassifier = "dev" }

tasks.processResources {
    properties(listOf("fabric.mod.json"),
        "id" to mod.id, "name" to mod.name, "license" to mod.license,
        "version" to mod.version, "minecraft" to common.mod.prop("mc_dep_fabric"),
        "authors" to mod.authors, "description" to mod.description,
        "archApi" to common.mod.dep("archApi_range_fabric"),
        "flywheel" to common.mod.dep("flywheel_range_fabric"),
        "veil" to common.mod.dep("veil_range_fabric"),
        "ponder" to common.mod.dep("ponder_range_fabric")
    )
}

sourceSets.main {
    resources { // include generated resources in resources
        srcDir("src/generated/resources")
        exclude("src/generated/resources/.cache")
    }
}

tasks.build {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
}

tasks.register<Copy>("buildAndCollect") {
    group = "versioned"
    description = "Must run through 'chiseledBuild'"
    from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
    dependsOn("build")
}

// Modmuss Publish
publishMods {
    file = tasks.remapJar.get().archiveFile
    changelog = ChangelogText.getChangelogText(rootProject).toString()
    displayName = "${common.mod.version} for $loaderCap $minecraft"
    modLoaders.addAll("fabric", "quilt")
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
