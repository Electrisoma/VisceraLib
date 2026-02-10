plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

loom {
    val awName = "${mod.mc}-${mod.id}_${mod.module}.accesswidener"
//    accessWidenerPath = commonNode.project.file("../../src/main/resources/accesswideners/$awName")

    // interface injection
//    with(stonecutterBuild.node.sibling("fabric")!!.project) {
//        fabricModJsonPath = file("../../src/main/resources/fabric.mod.json")
//    }

    @Suppress("UnstableApiUsage")
    mixin { useLegacyMixinAp = false }
}

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }
}

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })

    compileOnly("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")
    compileOnly("net.fabricmc:sponge-mixin:0.13.2+mixin.0.8.5")

    val mixinExtras = "io.github.llamalad7:mixinextras-common:${mod.ver("mixin_extras")}"
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