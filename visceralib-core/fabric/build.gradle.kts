plugins {
    `multiloader-loader`
    id("fabric-loom")
    id("dev.kikugie.fletching-table.fabric")
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

dependencies {

    setup(project)
    minecraft()
    mappings(layeredMappings())
    fabricLoader()

    embedFapi("fabric-api-base")

    modCompileOnly("com.terraformersmc:modmenu:${currentMod.dep("modmenu")}")
    modRuntimeOnly("com.terraformersmc:modmenu:${currentMod.dep("modmenu")}")

    runtimeFapi("fabric-resource-loader-v0")
    runtimeFapi("fabric-screen-api-v1")
    runtimeFapi("fabric-key-binding-api-v1")
    runtimeFapi("fabric-lifecycle-events-v1")
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

    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName = "${currentMod.id}_${currentMod.module}.refmap.json"
    }
}

tasks.named<ProcessResources>("processResources") {
    val commonResDir = project(common.project.parent!!.path).file("src/main/resources")
    val awFile = commonResDir.resolve("accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener")

    if (awFile.exists()) {
        from(awFile) {
            rename(awFile.name, "${currentMod.id}_${currentMod.module}.accesswidener")
            into("")
        }
    }
}