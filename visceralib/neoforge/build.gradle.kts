plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

val visceralibProjects = rootProject.subprojects.filter { it.path.startsWith(":visceralib-") }
val commonProjects     = visceralibProjects.filter { it.name == mod.mc && it.parent?.name == "common" }
val neoforgeProjects   = visceralibProjects.filter { it.name == mod.mc && it.parent?.name == "neoforge" }

val dependencyProjects = commonProjects + neoforgeProjects
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

configurations {
    val accessTransformersApi by creating
    val interfaceInjectionDataApi by creating
    val localRuntime by creating

    named("accessTransformers") { extendsFrom(accessTransformersApi) }
    named("accessTransformersElements") { extendsFrom(accessTransformersApi) }

    named("interfaceInjectionData") { extendsFrom(interfaceInjectionDataApi) }
    named("interfaceInjectionDataElements") { extendsFrom(interfaceInjectionDataApi) }

    runtimeClasspath.get().extendsFrom(localRuntime)
}

dependencies {
//    commonProjects.forEach {
//        api(it)
//    }

    neoforgeProjects.forEach {
        api(it)
        jarJar(it)
        "accessTransformersApi"(it)
        "interfaceInjectionDataApi"(it)
    }

    "localRuntime"(modrinth("better-modlist", mod.dep("better_modlist")))
}

neoForge {
    version = mod.dep("neoforge")

    val mdgRunDir = File("../../../../run")

    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${path})"
            gameDirectory = file(mdgRunDir.resolve("client").toString())
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${path})"
            gameDirectory = file(mdgRunDir.resolve("server").toString())
        }
    }

    parchment {
        mod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = mod.mc
        }
    }

    val commonResDir = projectDir.parentFile.parentFile.resolve("src/main/resources")

    interfaceInjectionData {
        from(commonResDir.resolve("interfaces.json"))
        publish(commonResDir.resolve("interfaces.json"))
    }

    accessTransformers {
        val sharedAt = commonResDir.resolve("META-INF/accesstransformer.cfg")
        if (sharedAt.exists())
            publish(sharedAt)
    }

    mods.register(mod.id) {
        sourceSet(sourceSets.main.get())
    }
}