@file:Suppress("UnstableApiUsage")

plugins {
    id("multiloader-common")
    id("fabric-loom")
    id("maven-publish")
    id("dev.kikugie.fletching-table.fabric")
}

val main: SourceSet? = sourceSets.getByName("main")

loom {
    accessWidenerPath = common.project.file(
        "../../src/main/resources/accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener"
    )

    mixin {
        useLegacyMixinAp = false
    }
}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

dependencies {
    minecraft(
        group = "com.mojang",
        name = "minecraft",
        version = currentMod.mc
    )

    mappings(loom.layered {
        officialMojangMappings()
        currentMod.depOrNull("parchment")?.let {
            parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${currentMod.mc}:$parchmentVersion@zip")
        }
    })

    modCompileOnly(
        group = "net.fabricmc",
        name = "fabric-loader",
        version = currentMod.dep("fabric-loader")
    )

    compileOnly(
        group = "org.spongepowered",
        name = "mixin",
        version = "0.8.5"
    )

    "io.github.llamalad7:mixinextras-common:0.5.0".let {
        compileOnly(it)
        annotationProcessor(it)
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
    afterEvaluate {
        val mainSourceSet = main!!

        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }
    }
}

apply(plugin = "maven-publish")

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifact(tasks.named("jar"))

            artifactId = "${currentMod.module}-$loader-${currentMod.mc}"
            group = currentMod.group
            version = "${currentMod.version}+mc${currentMod.mc}"
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/electrisoma/VisceraLib")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("mavenUsername")?.toString()
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("mavenToken")?.toString()
            }
        }
        mavenLocal()
    }
}