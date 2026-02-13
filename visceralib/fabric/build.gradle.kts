plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
}

val commonProjects = finder.dependOn(finder.common)
val fabricProjects = finder.dependOn(finder.fabric)

dependencies {
    minecraft("com.mojang:minecraft:${mod.mc}")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mod.mc}:${mod.ver("parchment")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")

    fapi.runtime("fabric-api-base")
    fapi.runtime("fabric-data-generation-api-v1")
    fapi.runtime("fabric-convention-tags-v2")
    fapi.runtime("fabric-particles-v1")

    fabricProjects.forEach {
        api(project(it.path, "namedElements"))
        include(it)
    }

    modLocalRuntime("com.terraformersmc:modmenu:${mod.ver("modmenu")}")

    fapi.runtime("fabric-resource-loader-v0")
    fapi.runtime("fabric-screen-api-v1")
    fapi.runtime("fabric-key-binding-api-v1")
    fapi.runtime("fabric-lifecycle-events-v1")
}

loom {
    val loomRunDir = File("../../run")

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