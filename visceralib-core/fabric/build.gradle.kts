plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }
}

dependencies {
    minecraft(project)
    mappings(layeredMappings(project))
    fabricLoader(project)

    embedFapi(project, "fabric-api-base")
    embedFapi(project, "fabric-lifecycle-events-v1")

    modCompileOnly("com.terraformersmc:modmenu:${mod.dep("modmenu")}")
    modLocalRuntime("com.terraformersmc:modmenu:${mod.dep("modmenu")}")

    runtimeFapi(project, "fabric-resource-loader-v0")
    runtimeFapi(project, "fabric-screen-api-v1")
    runtimeFapi(project, "fabric-key-binding-api-v1")
}

loom {
    val awName = "${mod.mc}-${mod.id}_${module}.accesswidener"
    accessWidenerPath = commonNode.project.file("../../src/main/resources/accesswideners/$awName")

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
    }
}

tasks.named<ProcessResources>("processResources") {
    val commonResDir = commonNode.project.parent!!.projectDir.resolve("src/main/resources")
    val awFile = commonResDir.resolve("accesswideners/${mod.mc}-${mod.id}_${module}.accesswidener")

    if (awFile.exists()) {
        from(awFile) {
            rename(awFile.name, "${mod.id}_${module}.accesswidener")
        }
    }
}