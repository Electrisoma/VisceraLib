plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
}

val currentMc = mod.mc

val dependencyProjects = rootProject.childProjects.values.filter {
    it.name.startsWith("visceralib-") &&
            it.name.endsWith("-common") &&
            it != project
}
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })

    compileOnly("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")

    dependencyProjects.forEach {
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
    val mainSourceSet = sourceSets.getByName("main")

    mainSourceSet.java.sourceDirectories.files.forEach {
        add(commonJava.name, it)
    }
    mainSourceSet.resources.sourceDirectories.files.forEach {
        add(commonResources.name, it)
    }
}