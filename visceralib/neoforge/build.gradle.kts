plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

val visceralibProjects = rootProject.subprojects.filter { it.path.startsWith(":visceralib-") }
val commonProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "common" }
val neoforgeProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "neoforge" }

val dependencyProjects = commonProjects + neoforgeProjects
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

dependencies {
    listImplementation(dependencyProjects)
    listJarJar(dependencyProjects)

    runtimeOnly(modrinth("better-modlist", project.mod.dep("better_modlist")))
}

neoForge {
    version = project.mod.dep("neoforge")

    val mdgRunDir = File("../../../../run")

    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${project.path})"
            gameDirectory = file(mdgRunDir.resolve("client").toString())
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${project.path})"
            gameDirectory = file(mdgRunDir.resolve("server").toString())
        }
    }

    parchment {
        project.mod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = project.mod.mc
        }
    }

    mods.register(project.mod.id) {
        sourceSet(sourceSets.main.get())
    }
}

sourceSets.main { resources.srcDir("src/generated/resources") }