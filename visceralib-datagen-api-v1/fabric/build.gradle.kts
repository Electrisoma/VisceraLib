plugins {
    `multiloader-loader`
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

val moduleBase = listOfNotNull(mod.id, mod.module, mod.suffix, mod.moduleVer)
    .filter { it.isNotBlank() }
    .joinToString("-")

val commonProject: Project = project(":$moduleBase-common")

val commonProjects: List<Project> = listOf(
    project(":visceralib-core-common")
)
val fabricProjects: List<Project> = listOf(
    project(":visceralib-core-fabric")
)
val dependencyProjects = commonProjects + fabricProjects
dependencyProjects.forEach { evaluationDependsOn(it.path) }

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

    val fapiVersion = "${mod.ver("fabric_api")}+${mod.mc}"

    listOf(
        "fabric-api-base",
        "fabric-data-generation-api-v1",
        "fabric-convention-tags-v2"
    ).forEach { module ->
        val dep = fabricApi.module(module, fapiVersion)
        modApi(dep)
        include(dep)
    }

    commonProjects.forEach {
        implementation(it)
    }

    fabricProjects.forEach {
        implementation(project(it.path, "namedElements"))
    }

    modCompileOnly("com.terraformersmc:modmenu:${mod.ver("modmenu")}")
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
    val commonResDir = commonProject.projectDir.resolve("src/main/resources")
    val awFile = commonResDir.resolve("accesswideners/${mod.mc}-$moduleBase.accesswidener")
    accessWidenerPath.set(awFile)

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
    val commonResDir = commonProject.projectDir.resolve("src/main/resources")
    val awFile = commonResDir.resolve("accesswideners/${mod.mc}-$moduleBase.accesswidener")

    if (awFile.exists()) {
        from(awFile) {
            rename(awFile.name, "${mod.id}_$moduleBase.accesswidener")
        }
    }
}