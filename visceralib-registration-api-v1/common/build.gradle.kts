plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

val dependencyProjects: List<Project> = listOf(
    project(":visceralib-core:common:${mod.mc}")
)
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

loom {
    val awName = "${mod.mc}-${mod.id}_${module}.accesswidener"
    accessWidenerPath = commonNode.project.file("../../src/main/resources/accesswideners/$awName")

    // interface injection
    stonecutterBuild.node.sibling("fabric")?.project?.let { fabricProj ->
        fabricModJsonPath = fabricProj.file("../../src/main/resources/fabric.mod.json")
    }

    @Suppress("UnstableApiUsage")
    mixin { useLegacyMixinAp = false }
}

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }
}

dependencies {
    minecraft(project)
    mappings(layeredMappings(project))

    compileOnly("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    compileOnly("net.fabricmc:sponge-mixin:0.13.2+mixin.0.8.5")

    val mixinExtras = "io.github.llamalad7:mixinextras-common:${mod.dep("mixin_extras")}"
    annotationProcessor(mixinExtras)
    compileOnly(mixinExtras)

    dependencyProjects.forEach {
        implementation(it)
    }
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

tasks.matching { it.name == "genSourcesWithVineflower" }.configureEach {
    val corePath = ":visceralib-core:common:${stonecutterBuild.current.version}"
    val coreProject = rootProject.findProject(corePath)

    if (coreProject != null) {
        mustRunAfter(coreProject.tasks.matching { it.name == "validateAccessWidener" })
    }
}