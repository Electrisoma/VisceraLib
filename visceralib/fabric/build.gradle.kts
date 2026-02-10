plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
}

val vProjects = rootProject.childProjects.values.filter { it.name.startsWith("visceralib-") }
val commonProjects = vProjects.filter { it.name.endsWith("-common") }
val fabricProjects = vProjects.filter {
    it.name.endsWith("-fabric") && it != project
}

val dependencyProjects = commonProjects + fabricProjects
dependencyProjects.forEach { evaluationDependsOn(it.path) }

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")

    val fapiVersion = "${mod.ver("fabric_api")}+${mod.mc}"

    listOf(
        "fabric-api-base",
        "fabric-data-generation-api-v1",
        "fabric-convention-tags-v2",
        "fabric-particles-v1"
    ).forEach { module ->
        modRuntimeOnly(fabricApi.module(module, fapiVersion))
    }

    fabricProjects.forEach {
        api(project(it.path, "namedElements"))
        include(it)
    }

    modLocalRuntime("com.terraformersmc:modmenu:${mod.ver("modmenu")}")

    listOf(
        "fabric-resource-loader-v0",
        "fabric-screen-api-v1",
        "fabric-key-binding-api-v1",
        "fabric-lifecycle-events-v1"
    ).forEach { module ->
        modRuntimeOnly(fabricApi.module(module, fapiVersion))
    }
}

loom {
    val loomRunDir = File("../../../run")

    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            runDir(loomRunDir.resolve("client").toString())
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            runDir(loomRunDir.resolve("server").toString())
        }
    }
}