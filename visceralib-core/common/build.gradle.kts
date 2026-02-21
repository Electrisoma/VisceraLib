plugins {
    alias(libs.plugins.multiloader.common)
    alias(libs.plugins.loader.loom)
    alias(libs.plugins.fletchingtable.fab)
}

loom {
    accessWidenerPath.set(mod.commonAW)

    // interface injection
    rootProject.findProject("${mod.moduleBase}-fabric")?.let {
        fabricModJsonPath.set(it.file("src/main/resources/fabric.mod.json"))
    }
}

fletchingTable {
    j52j.register("main") { extension("json", "**/*.json5") }
}

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })

    compileOnly("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")
    compileOnly("net.fabricmc:sponge-mixin:${mod.ver("sponge_mixin")}")

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
    sourceSets.main.get().let { main ->
        main.java.sourceDirectories.forEach { add(commonJava.name, it) }
        main.resources.sourceDirectories.forEach { add(commonResources.name, it) }
    }
}