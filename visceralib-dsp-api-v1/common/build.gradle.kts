plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    alias(libs.plugins.fletchingtable.fab)
}

val commonProjects = finder.dependOn(listOf(project(":visceralib-core-common")))

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }
}

loom {
    accessWidenerPath.set(mod.commonAW)

    // interface injection
    rootProject.findProject("${mod.moduleBase}-fabric")?.let {
        fabricModJsonPath.set(it.file("src/main/resources/fabric.mod.json"))
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    mappings(mapping.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })

    compileOnly("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")
    compileOnly("net.fabricmc:sponge-mixin:${mod.ver("sponge_mixin")}")

    val mixinExtras = "io.github.llamalad7:mixinextras-common:${mod.ver("mixin_extras")}"
    annotationProcessor(mixinExtras)
    compileOnly(mixinExtras)

    commonProjects.forEach { implementation(it) }
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
    sourceSets.main.get().let { main ->
        main.java.sourceDirectories.forEach { add(commonJava.name, it) }
        main.resources.sourceDirectories.forEach { add(commonResources.name, it) }
    }
}