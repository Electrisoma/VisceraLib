plugins {
    alias(libs.plugins.multiloader.loader)
    alias(libs.plugins.loader.mdg)
    alias(libs.plugins.fletchingtable.neo)
}

val dependencyProjects = finder.dependOn(listOf(
    project(":visceralib-core-common"),
    project(":visceralib-core-neoforge")
))

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }

    accessConverter.register("main") {
        add("accesswideners/${mod.mc}-${mod.moduleBase}.accesswidener")
    }
}

configurations {
    create("localRuntime")
    runtimeClasspath.get().extendsFrom(configurations["localRuntime"])
}

dependencies {
    dependencyProjects.forEach { implementation(it) }

    compileOnly(repos.modrinth("better-modlist", mod.ver("better_modlist")))
    "localRuntime"(repos.modrinth("better-modlist", mod.ver("better_modlist")))
}

val syncAT = tasks.register<Copy>("syncAT") {
    dependsOn(tasks.processResources)
    from(layout.buildDirectory.dir("resources/main/META-INF"))
    include("accesstransformer.cfg")
    into(layout.buildDirectory.dir("generated/at"))
}

neoForge {
    version = mod.ver("neoforge")

    interfaceInjectionData {
        mod.commonResource("interfaces.json").let {
            from(it)
            publish(it)
        }
    }

    accessTransformers {
        publish(syncAT.map { it.destinationDir.resolve("accesstransformer.cfg") })
    }

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

    mods.register(mod.moduleBase) {
        sourceSet(sourceSets.main.get())
    }
}

tasks.processResources {
    exclude("**/${mod.moduleBase}.accesswidener")
}