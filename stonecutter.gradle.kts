plugins {
    `maven-publish`
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.9.+" apply false
    id("architectury-plugin") version "3.4.+" apply false
    id("com.gradleup.shadow") version "8.3.5" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.7.4" apply false
    id("dev.ithundxr.silk") version "0.11.+"
}

stonecutter active "1.21.1" /* [SC] DO NOT EDIT */
//stonecutter.automaticPlatformConstants = true

stonecutter.parameters { // With flat-arch setup, where versions are in versions/{version}-{loader}:
    val loader = metadata.project.substringAfterLast("-")
    consts(loader, "fabric", "neoforge")
}

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledPublish", stonecutter.chiseled) {
    group = "project"
    ofTask("publish")
}

for (it in stonecutter.tree.branches) {
    if (it.id.isEmpty()) continue
    val loader = it.id.upperCaseFirst()

    // Builds loader-specific versions into `build/libs/{mod.version}/{loader}`
    stonecutter registerChiseled tasks.register("chiseledBuild$loader", stonecutter.chiseled) {
        group = "project"
        versions { branch, _ -> branch == it.id }
        ofTask("buildAndCollect")
    }

    // Publishes loader-specific versions
    stonecutter registerChiseled tasks.register("chiseledPublish$loader", stonecutter.chiseled) {
        group = "project"
        versions { branch, _ -> branch == it.id }
        ofTask("publish")
    }
}

for (it in stonecutter.tree.nodes) { // Runs active versions for each loader
    if (it.metadata != stonecutter.current || it.branch.id.isEmpty()) continue
    val types = listOf("Client", "Server")
    val loader = it.branch.id.upperCaseFirst()
    for (type in types) it.project.tasks.register("runActive$type$loader") {
        group = "project"
        dependsOn("run$type")
    }
}
subprojects {
    apply(plugin = "maven-publish")
    repositories {
        mavenCentral()
        maven("https://maven.parchmentmc.org")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")

        maven("https://maven.blamejared.com")
        maven("https://maven.createmod.net/")
        maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        maven("https://jitpack.io/")
        maven("https://api.modrinth.com/maven")

        exclusiveContent {
            forRepository { maven { url = uri("https://api.modrinth.com/maven") } }
            filter { includeGroup("maven.modrinth") }
        }
        exclusiveContent {
            forRepository { maven { url = uri("https://cursemaven.com") } }
            filter { includeGroup("curse.maven") }
        }
        flatDir{ dir("libs") }
    }
}