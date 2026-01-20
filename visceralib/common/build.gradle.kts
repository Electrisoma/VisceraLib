plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
}

val visceralibProjects = rootProject.subprojects.filter { it.path.startsWith(":visceralib-") }
val commonProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "common" }

val dependencyProjects = commonProjects
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

dependencies {
    minecraft(project)
    mappings(layeredMappings(project))

    compileOnly("net.fabricmc:fabric-loader:${project.mod.dep("fabric-loader")}")

    listImplementation(dependencyProjects)
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    val mainSourceSet = sourceSets.getByName("main")

    mainSourceSet.java.sourceDirectories.files.forEach {
        add(commonJava.name, it)
    }
    mainSourceSet.resources.sourceDirectories.files.forEach {
        add(commonResources.name, it)
    }
}

tasks.matching { it.name == "genSourcesWithVineflower" }.configureEach {
    val corePath = ":visceralib-core:common:${project.stonecutterBuild.current.version}"
    val coreProject = rootProject.findProject(corePath)

    if (coreProject != null) {
        mustRunAfter(coreProject.tasks.matching { it.name == "validateAccessWidener" })
    }
}