@file:Suppress("UnstableApiUsage")

plugins {
    `maven-publish`
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("me.modmuss50.mod-publish-plugin")
    id("dev.ithundxr.silk")
    `java-library`
}

val minecraft = stonecutter.current.version

val ci = System.getenv("CI")?.toBoolean() ?: false
val release = System.getenv("RELEASE")?.toBoolean() ?: false
val nightly = ci && !release
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()

version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraft}-common${if (nightly) "-build.${buildNumber}" else ""}"
group = "${group}.common"
base.archivesName.set(mod.id)

architectury.common(stonecutter.tree.branches.mapNotNull {
    if (stonecutter.current.project !in it) null
    else it.project.prop("loom.platform")
})

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    silentMojangMappingsLicense()
    accessWidenerPath = rootProject.file("src/main/resources/${mod.id}.accesswidener")
    runConfigs.all {
        isIdeConfigGenerated = true
        runDir = "../../../run"
        vmArgs("-Dmixin.debug.export=true")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment("org.parchmentmc.data:parchment-$minecraft:${mod.dep("parchment_version")}@zip")
    })

    // fabric stuffs
    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api_version")}")

    // general stuff
    //modImplementation("dev.architectury:architectury:${mod.dep("archApi")}")

    "io.github.llamalad7:mixinextras-common:${mod.dep("mixin_extras")}".let {
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

