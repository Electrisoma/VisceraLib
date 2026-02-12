plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

val vProjects = rootProject.childProjects.values.filter { it.name.startsWith("visceralib-") }
val commonProjects = vProjects.filter { it.name.endsWith("-common") }
val neoforgeProjects = vProjects.filter {
    it.name.endsWith("-neoforge") && it != project
}

val dependencyProjects = commonProjects + neoforgeProjects
dependencyProjects.forEach { evaluationDependsOn(it.path) }

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
    neoforgeProjects.forEach {
        api(it)
        jarJar(it)
        "accessTransformersApi"(it)
        "interfaceInjectionDataApi"(it)
    }

    "localRuntime"(modrinth("better-modlist", mod.ver("better_modlist")))
}

neoForge {
    version = mod.ver("neoforge")

    val mdgRunDir = File("../../run")

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
        mod.ver("parchment").let {
            mappingsVersion = it
            minecraftVersion = mod.mc
        }
    }

    val commonResDir = projectDir.resolve("src/main/resources")

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