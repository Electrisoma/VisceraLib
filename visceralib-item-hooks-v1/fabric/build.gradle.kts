plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    alias(libs.plugins.fletchingtable.fab)
}

val commonProjects = finder.dependOn(listOf(project(":visceralib-core-common")))
val fabricProjects = finder.dependOn(listOf(project(":visceralib-core-fabric")))

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

    modImplementation("net.fabricmc:fabric-loader:${mod.ver("fabric_loader")}")

    fapi.embed("fabric-api-base")
    fapi.embed("fabric-resource-loader-v0")
    fapi.embed("fabric-item-api-v1")

    commonProjects.forEach { implementation(it) }
    fabricProjects.forEach { implementation(project(it.path, "namedElements")) }

    modCompileOnly("com.terraformersmc:modmenu:${mod.ver("modmenu")}")
    modLocalRuntime("com.terraformersmc:modmenu:${mod.ver("modmenu")}")

    fapi.runtime("fabric-resource-loader-v0")
    fapi.runtime("fabric-screen-api-v1")
    fapi.runtime("fabric-key-binding-api-v1")
    fapi.runtime("fabric-lifecycle-events-v1")
}

loom {
    accessWidenerPath.set(mod.commonAW)

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

tasks.named<ProcessResources>("processResources") {
    if (mod.commonAW.exists()) {
        from(mod.commonAW) {
            rename(mod.commonAW.name, "${mod.id}_${mod.moduleBase}.accesswidener")
        }
    }
}