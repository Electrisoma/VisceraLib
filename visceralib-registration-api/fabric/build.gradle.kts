plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

val commonProjects: List<Project> = listOf(
    project(":visceralib-core:common:${project.mod.mc}")
)
val fabricProjects: List<Project> = listOf(
    project(":visceralib-core:fabric:${project.mod.mc}")
)
val dependencyProjects = commonProjects + fabricProjects
dependencyProjects.forEach { evaluationDependsOn(it.path) }

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }
}

dependencies {
    minecraft(project)
    mappings(layeredMappings(project))
    fabricLoader(project)

    embedFapi(project, "fabric-api-base")
    embedFapi(project, "fabric-particles-v1")
    embedFapi(project, "fabric-registry-sync-v0")

    listImplementation(commonProjects)
    listImplementation(fabricProjects, "namedElements")

    modOptional(
        "com.terraformersmc:modmenu:${project.mod.dep("modmenu")}",
        project.findProperty("run_modmenu")?.toString()?.toBoolean() ?: false
    )

    runtimeFapi(project, "fabric-resource-loader-v0")
    runtimeFapi(project, "fabric-screen-api-v1")
    runtimeFapi(project, "fabric-key-binding-api-v1")
    runtimeFapi(project, "fabric-lifecycle-events-v1")
}

loom {
    val awName = "${project.mod.mc}-${project.mod.id}_${project.module}.accesswidener"
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

tasks {
    named<ProcessResources>("processResources") {
        val commonResDir = commonNode.project.parent!!.projectDir.resolve("src/main/resources")
        val awFile = commonResDir.resolve("accesswideners/${project.mod.mc}-${project.mod.id}_${project.module}.accesswidener")

        if (awFile.exists()) {
            from(awFile) {
                rename(awFile.name, "${project.mod.id}_${project.module}.accesswidener")
            }
        }
    }
    matching { it.name == "genSourcesWithVineflower" }.configureEach {
        val corePath = ":visceralib-core:fabric:${project.stonecutterBuild.current.version}"
        val coreProject = rootProject.findProject(corePath)

        if (coreProject != null) {
            mustRunAfter(coreProject.tasks.matching { it.name == "validateAccessWidener" })
        }
    }
}