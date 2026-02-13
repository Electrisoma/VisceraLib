plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
}

val commonProjects = finder.dependOn(finder.common)

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })

    compileOnly("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")

    commonProjects.forEach {
        api(it)
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
    sourceSets.main.get().let { main ->
        main.java.sourceDirectories.forEach { add(commonJava.name, it) }
        main.resources.sourceDirectories.forEach { add(commonResources.name, it) }
    }
}