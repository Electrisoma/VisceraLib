plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge")
}

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }

    accessConverter.register("main") {
        add("accesswideners/${project.mod.mc}-${project.mod.id}_${project.module}.accesswidener")
    }
}

dependencies {
    optional(
        modrinth("better-modlist", project.mod.dep("better_modlist")),
        project.findProperty("run_better_modlist")?.toString()?.toBoolean() ?: false
    )
}

neoForge {
    version = project.mod.dep("neoforge")

    val commonResDir = commonNode.project.parent!!.projectDir.resolve("src/main/resources")
    interfaceInjectionData {
        from(commonResDir.resolve("interfaces.json"))
        publish(commonResDir.resolve("interfaces.json"))
    }

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
        project.mod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = project.mod.mc
        }
    }

    mods.register("${project.mod.id}_${project.module}") {
        sourceSet(sourceSets.main.get())
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("${project.mod.id}_${project.module}.accesswidener")
    }
}