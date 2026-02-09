import dev.kikugie.stonecutter.Identifier

plugins {
    id("multiloader-common")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

dependencies {
    val commonPath = commonNode.hierarchy.toString()
    compileOnly(project(path = commonPath))
    commonJava(project(path = commonPath, configuration = "commonJava"))
    commonResources(project(path = commonPath, configuration = "commonResources"))
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    jar {
        exclude("accesswideners", "accesswideners/**")
    }

    named<JavaCompile>("compileJava") {
        dependsOn(commonJava)
        source(commonJava)
    }

    named<ProcessResources>("processResources") {
        dependsOn(commonResources)
        from(commonResources)
    }
}

afterEvaluate {
    val loader = loader

    val name = findProperty("module")?.toString()?.takeIf { it.isNotBlank() }
    val suffix = findProperty("suffix")?.toString()?.takeIf { it.isNotBlank() }
    val ver = findProperty("module_version")?.toString()?.takeIf { it.isNotBlank() }

    val base = listOfNotNull(name, suffix).joinToString("-")
    val configLabel = if (ver != null) "$base/$ver" else base

    stonecutterBuild.constants.match(
        loader as Identifier,
        "fabric",
        "neoforge"
    )

    extensions.findByType<net.neoforged.moddevgradle.dsl.NeoForgeExtension>()?.apply {
        runs.all {
            if (configLabel.isNotEmpty()) {
                ideFolderName.set(configLabel)
            }
        }
    }

    extensions.findByType<net.fabricmc.loom.api.LoomGradleExtensionAPI>()?.apply {
        runs {
            all {
                if (configLabel.isNotEmpty()) {
                    ideConfigFolder.set(configLabel)
                }
                ideConfigGenerated(true)
            }
            getByName("client") {
                programArgs("--username", "dev")
            }
        }
    }
}