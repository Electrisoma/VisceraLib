plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

val visceralibProjects = rootProject.subprojects.filter { it.path.startsWith(":visceralib-") }
val commonProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "common" }
val neoforgeProjects = visceralibProjects.filter { it.name == project.mod.mc && it.parent?.name == "neoforge" }

val dependencyProjects = commonProjects + neoforgeProjects
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

configurations {
    val accessTransformersApi by creating
    val interfaceInjectionDataApi by creating

    named("accessTransformers") { extendsFrom(accessTransformersApi) }
    named("accessTransformersElements") { extendsFrom(accessTransformersApi) }

    named("interfaceInjectionData") { extendsFrom(interfaceInjectionDataApi) }
    named("interfaceInjectionDataElements") { extendsFrom(interfaceInjectionDataApi) }
}

dependencies {
    commonProjects.forEach { sub ->
        api(sub)
    }

    neoforgeProjects.forEach { sub ->
        api(sub)
        jarJar(sub)
        "accessTransformersApi"(sub)
        "interfaceInjectionDataApi"(sub)
    }

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

    val commonResDir = project.projectDir.parentFile.parentFile.resolve("src/main/resources")

    interfaceInjectionData {
        from(commonResDir.resolve("interfaces.json"))
        publish(commonResDir.resolve("interfaces.json"))
    }

    accessTransformers {
        val sharedAt = commonResDir.resolve("META-INF/accesstransformer.cfg")
        if (sharedAt.exists())
            publish(sharedAt)
    }

    mods.register(project.mod.id) {
        sourceSet(sourceSets.main.get())
    }
}