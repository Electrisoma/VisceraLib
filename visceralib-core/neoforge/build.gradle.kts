@file:Suppress("UnstableApiUsage")

plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge")
}

val main: SourceSet? = sourceSets.getByName("main")

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

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
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("${currentMod.id}_${currentMod.module}.accesswidener")
    }
}