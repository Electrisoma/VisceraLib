plugins {
    alias(libs.plugins.multiloader.common)
    alias(libs.plugins.loader.mdg)
}

val commonProjects = finder.dependOn(finder.common)

dependencies {
    commonProjects.forEach {
        api(it)
        accessTransformersApi(it)
        interfaceInjectionDataApi(it)
    }
}

neoForge {
    neoFormVersion = mod.ver("neoform")

    parchment {
        mod.ver("parchment").let {
            mappingsVersion = it
            minecraftVersion = mod.mc
        }
    }

    interfaceInjectionData {
        mod.commonResource("interfaces.json").let {
            from(it)
            publish(it)
        }
    }

    accessTransformers {
        mod.commonResource("META-INF/accesstransformer.cfg").takeIf { it.exists() }?.let {
            publish(it)
        }
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