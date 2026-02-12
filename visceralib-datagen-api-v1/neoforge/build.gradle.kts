plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge")
}

val moduleBase = listOfNotNull(mod.id, mod.module, mod.suffix, mod.moduleVer)
    .filter { it.isNotBlank() }
    .joinToString("-")

val commonProject: Project = project(":$moduleBase-common")

val dependencyProjects: List<Project> = listOf(
    project(":visceralib-core-common"),
    project(":visceralib-core-neoforge")
)
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }

    accessConverter.register("main") {
        add("accesswideners/${mod.mc}-$moduleBase.accesswidener")
    }
}

configurations {
    create("localRuntime")
    runtimeClasspath.get().extendsFrom(configurations["localRuntime"])
}

dependencies {
    dependencyProjects.forEach {
        implementation(it)
    }

    compileOnly(modrinth("better-modlist", mod.ver("better_modlist")))
    "localRuntime"(modrinth("better-modlist", mod.ver("better_modlist")))
}

val syncAT = tasks.register<Copy>("syncAT") {
    dependsOn(tasks.processResources)
    from(layout.buildDirectory.dir("resources/main/META-INF"))
    include("accesstransformer.cfg")
    into(layout.buildDirectory.dir("generated/at"))
}

neoForge {
    version = mod.ver("neoforge")

    val commonResDir = commonProject.projectDir.resolve("src/main/resources")

    interfaceInjectionData {
        from(commonResDir.resolve("interfaces.json"))
        publish(commonResDir.resolve("interfaces.json"))
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

    mods.register(moduleBase) {
        sourceSet(sourceSets.main.get())
    }
}

tasks.processResources {
    exclude("**/$moduleBase.accesswidener")
}