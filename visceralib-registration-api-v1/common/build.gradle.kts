plugins {
    id("multiloader-common")
    id("net.fabricmc.fabric-loom-remap")
    id("dev.kikugie.fletching-table.fabric")
}

val moduleBase = listOfNotNull(mod.id, mod.module, mod.suffix, mod.moduleVer)
    .filter { it.isNotBlank() }
    .joinToString("-")

val dependencyProjects: List<Project> = listOf(
    project(":visceralib-core-common")
)
dependencyProjects.forEach { project.evaluationDependsOn(it.path) }

loom {
    val awName = "${mod.mc}-$moduleBase.accesswidener"
    accessWidenerPath = file("src/main/resources/accesswideners/$awName")

    // interface injection
    val fabricProject: Project? = rootProject.findProject("$moduleBase-fabric")
    fabricProject?.let { fabricModJsonPath.set(it.file("src/main/resources/fabric.mod.json")) }

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

    dependencyProjects.forEach {
        implementation(it)
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