plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("dev.kikugie.fletching-table.neoforge")
}

val main: SourceSet? = sourceSets.getByName("main")

val visceraLibCorePathCommon: String = ":visceralib-core:common:${currentMod.mc}"
val visceraLibCorePathLoader: String = ":visceralib-core:neoforge:${currentMod.mc}"

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

val dependencyProjects: List<Project> = listOf(
    project(visceraLibCorePathCommon),
    project(visceraLibCorePathLoader)
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
    dependencyProjects.forEach {
        implementation(it)
    }
}

val selfModule = project.parent?.parent ?: project

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
        register("data") {
            data()
            ideName = "NeoForge Datagen (${project.path})"
            gameDirectory = file("../../../../run/data")

            val outputPath = project.projectDir.parentFile.resolve("src/generated/resources")
            val commonPath = project.projectDir.parentFile.parentFile.resolve("common/src/main/resources")

            programArguments.addAll(
                "--mod",
                currentMod.id,
                "--all",
                "--output", outputPath.absolutePath,
                "--existing", commonPath.absolutePath
            )
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

println("NEOFORGE LINE " + selfModule.file("neoforge/src/generated/resources").absolutePath)
println("COMMON LINE " + selfModule.file("common/src/main/resources").absolutePath)

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