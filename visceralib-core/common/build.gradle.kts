plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

loom {
    val awName = "${project.mod.mc}-${project.mod.id}_${project.module}.accesswidener"
    accessWidenerPath = commonNode.project.file("../../src/main/resources/accesswideners/$awName")

    // interface injection
    with(stonecutterBuild.node.sibling("fabric")!!.project) {
        fabricModJsonPath = file("../../src/main/resources/fabric.mod.json")
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

    compileOnly("net.fabricmc:fabric-loader:${project.mod.dep("fabric-loader")}")
    compileOnly("net.fabricmc:sponge-mixin:0.13.2+mixin.0.8.5")

    val mixinExtras = "io.github.llamalad7:mixinextras-common:${project.mod.dep("mixin_extras")}"
    annotationProcessor(mixinExtras)
    compileOnly(mixinExtras)
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