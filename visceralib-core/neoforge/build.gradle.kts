plugins {
    `multiloader-loader`
    id("net.neoforged.moddev")
    id("maven-publish")
    id("dev.kikugie.fletching-table.neoforge")
}

val main: SourceSet? = sourceSets.getByName("main")

val commonProjectPath: String = project.parent!!.parent!!.path + ":common:"
val versionedCommonProjectPath: String = commonProjectPath + currentMod.mc

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }

    accessConverter.register("main") {
        add("accesswideners/${currentMod.mc}-${currentMod.id}_${currentMod.module}.accesswidener")
    }
}

neoForge {
    enable {
        version = currentMod.dep("neoforge")
    }
}

neoForge {
    runs {
        register("client") {
            client()
            ideName = "NeoForge Client (${project.path})"
            gameDirectory = file("../../../../run/client")
        }
        register("server") {
            server()
            ideName = "NeoForge Server (${project.path})"
            gameDirectory = file("../../../../run/server")
        }
    }

    parchment {
        currentMod.depOrNull("parchment")?.let {
            mappingsVersion = it
            minecraftVersion = currentMod.mc
        }
    }

    mods {
        register("${currentMod.id}_${currentMod.module}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

tasks {
    processResources {
        exclude("${currentMod.id}_${currentMod.module}.accesswidener")
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