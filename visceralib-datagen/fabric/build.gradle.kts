plugins {
    `multiloader-loader`
    id("fabric-loom")
    id("dev.kikugie.fletching-table.fabric")
}

val commonProjects: List<Project> = listOf(
    project(":visceralib-core:common:${currentMod.mc}")
)
val fabricProjects: List<Project> = listOf(
    project(":visceralib-core:fabric:${currentMod.mc}")
)
val dependencyProjects = commonProjects + fabricProjects

dependencyProjects.forEach {
    project.evaluationDependsOn(it.path)
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

    listImplementation(commonProjects)
    listImplementation(fabricProjects, "namedElements")

    embedFapi("fabric-api-base")
    embedFapi("fabric-data-generation-api-v1")
    embedFapi("fabric-convention-tags-v2")

    modCompileOnly("com.terraformersmc:modmenu:${currentMod.dep("modmenu")}")
    if(property("run_modmenu").toString().toBoolean())
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
            ideConfigGenerated(true)
            runDir(loomRunDir.resolve("client").toString())
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir(loomRunDir.resolve("server").toString())
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

    from(awFile.parentFile) {
        include(awFile.name)
        rename(awFile.name, "${currentMod.id}_${currentMod.module}.accesswidener")
        into("")
    }
}