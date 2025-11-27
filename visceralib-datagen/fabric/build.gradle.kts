@file:Suppress("UnstableApiUsage")

plugins {
    `multiloader-loader`
    id("fabric-loom")
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

stonecutter {

}

dependencies {
    minecraft("com.mojang:minecraft:${currentMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        currentMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${currentMod.mc}:$parchmentVersion@zip")
        }
    })

    modImplementation("net.fabricmc:fabric-loader:${currentMod.dep("fabric-loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${currentMod.dep("fabric-api")}+${currentMod.mc}")
}

loom {
    accessWidenerPath = common.project.file("../../src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}.accesswidener")

    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
        }
    }

    mixin {
        defaultRefmapName = "${currentMod.id}.refmap.json"
    }
}

tasks.named<ProcessResources>("processResources") {
    val awFile = project(":common").file("src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}.accesswidener")

    from(awFile.parentFile) {
        include(awFile.name)
        rename(awFile.name, "${currentMod.id}.accesswidener")
        into("")
    }
}