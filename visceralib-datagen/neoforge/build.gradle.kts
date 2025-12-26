plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge")
}

val dependencyProjects: List<Project> = listOf(
    project(":visceralib-core:common:${currentMod.mc}"),
    project(":visceralib-core:neoforge:${currentMod.mc}")
)

dependencyProjects.forEach {
    project.evaluationDependsOn(it.path)
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }

    accessConverter.register("main") {
        add("accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener")
    }
}

neoForge {
    enable {
        version = currentMod.dep("neoforge")
    }
}

dependencies {
    listImplementation(dependencyProjects)
    compileOnly(currentMod.modrinth("better-modlist", currentMod.dep("better_modlist")))
    if(property("run_better_modlist").toString().toBoolean())
        runtimeOnly(currentMod.modrinth("better-modlist", currentMod.dep("better_modlist")))
}

neoForge {
    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${project.path})"
            gameDirectory = file("../../../../run/client")
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${project.path})"
            gameDirectory = file("../../../../run/server")
        }
    }

    parchment {
        currentMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = currentMod.mc
        }
    }

    mods {
        register("${currentMod.id}_${currentMod.module}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources {
        srcDir(project.projectDir.resolve("src/generated/resources"))
        exclude("src/generated/resources/.cache")
    }
}

tasks {
    processResources {
        exclude("${currentMod.id}_${currentMod.module}.accesswidener")
    }
}