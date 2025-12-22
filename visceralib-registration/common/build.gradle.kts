plugins {
    id("multiloader-common")
    id("fabric-loom")
    id("dev.kikugie.fletching-table.fabric")
}

val dependencyProjects: List<Project> = listOf(
    project(":visceralib-core:common:${currentMod.mc}")
)

dependencyProjects.forEach {
    project.evaluationDependsOn(it.path)
}

loom {
    accessWidenerPath = common.project.file(
        "../../src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener"
    )

    @Suppress("UnstableApiUsage")
    mixin {
        useLegacyMixinAp = false
    }
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

dependencies {

    setup(project)
    minecraft()
    mappings(layeredMappings())

    compileOnly("net.fabricmc:fabric-loader:${currentMod.dep("fabric-loader")}")
    compileOnly("org.spongepowered:mixin:0.8.5")

    "io.github.llamalad7:mixinextras-common:0.5.0".let {
        compileOnly(it)
        annotationProcessor(it)
    }

    listImplementation(dependencyProjects)
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    val mainSourceSet = sourceSets.getByName("main")

    mainSourceSet.java.sourceDirectories.files.forEach {
        add(commonJava.name, it)
    }
    mainSourceSet.resources.sourceDirectories.files.forEach {
        add(commonResources.name, it)
    }
}