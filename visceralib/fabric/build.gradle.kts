plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
}

val visceralibProjects = rootProject.subprojects.filter { it.path.startsWith(":visceralib-") }
val commonProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "common" }
val fabricProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "fabric" }

val dependencyProjects = commonProjects + fabricProjects
dependencyProjects.forEach { evaluationDependsOn(it.path) }

dependencies {
    minecraft(project)
    mappings(layeredMappings(project))
    fabricLoader(project)

    runtimeFapi(project, "fabric-api-base")
    runtimeFapi(project, "fabric-data-generation-api-v1")
    //? != 1.20.1 {
    // runtimeFapi(project, "fabric-convention-tags-v2")
    // ?//}
    //? == 1.20.1 {
    // runtimeFapi(project, "fabric-convention-tags-v1")
    // ?//}
    runtimeFapi(project, "fabric-particles-v1")

    listImplementation(commonProjects)
    listImplementation(fabricProjects, "namedElements")
    listInclude(fabricProjects)

    modRuntimeOnly("com.terraformersmc:modmenu:${project.mod.dep("modmenu")}")

    runtimeFapi(project, "fabric-resource-loader-v0")
    runtimeFapi(project, "fabric-screen-api-v1")
    runtimeFapi(project, "fabric-key-binding-api-v1")
    runtimeFapi(project, "fabric-lifecycle-events-v1")
}

loom {
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

tasks.matching { it.name == "genSourcesWithVineflower" }.configureEach {
    val corePath = ":visceralib-core:common:${project.stonecutterBuild.current.version}"
    val coreProject = rootProject.findProject(corePath)

    if (coreProject != null) {
        mustRunAfter(coreProject.tasks.matching { it.name == "validateAccessWidener" })
    }
}