plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
}

val commonProjects = finder.dependOn(finder.common)
val neoforgeProjects = finder.dependOn(finder.neoforge)

configurations {
    val accessTransformersApi by creating
    val interfaceInjectionDataApi by creating
    val localRuntime by creating

    listOf("accessTransformers", "accessTransformersElements").forEach {
        named(it) { extendsFrom(accessTransformersApi) }
    }

    listOf("interfaceInjectionData", "interfaceInjectionDataElements").forEach {
        named(it) { extendsFrom(interfaceInjectionDataApi) }
    }

    runtimeClasspath.get().extendsFrom(localRuntime)
}

dependencies {
    neoforgeProjects.forEach {
        api(it)
        jarJar(it)
        "accessTransformersApi"(project(it.path, "accessTransformersElements"))
        "interfaceInjectionDataApi"(project(it.path, "interfaceInjectionDataElements"))
    }

    "localRuntime"(repos.modrinth("better-modlist", mod.ver("better_modlist")))
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

    interfaceInjectionData {
        mod.commonResource("interfaces.json").let {
            from(it)
            publish(it)
        }
    }

    accessTransformers {
        mod.commonResource("META-INF/accesstransformer.cfg").takeIf { it.exists() }?.let {
            publish(it)
        }
    }

    mods.register(mod.id) {
        sourceSet(sourceSets.main.get())
    }
}