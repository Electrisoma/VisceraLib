@file:Suppress("UnstableApiUsage")

plugins {
    `multiloader-loader`
    id("fabric-loom")
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