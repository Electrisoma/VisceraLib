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
    val moduleBaseName = project.name.substringBeforeLast("-")
    val commonProjectName = "$moduleBaseName-common"

    val commonProject = rootProject.childProjects[commonProjectName]
        ?: throw IllegalStateException("Could not find common sibling '$commonProjectName' for ${project.name}")

    val commonPath = commonProject.path

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
    val loader = props.loader

    val base = listOfNotNull(
        mod.module.takeIf { it.isNotBlank() },
        mod.suffix.takeIf { it.isNotBlank() }
    ).joinToString("-")

    val specificPath = listOfNotNull(
        base.takeIf { it.isNotBlank() },
        mod.moduleVer.takeIf { it.isNotBlank() }
    ).joinToString("/")

    val configLabel = specificPath.ifBlank { mod.id }

    extensions.findByName("neoForge")?.let {
        configure<net.neoforged.moddevgradle.dsl.NeoForgeExtension> {
            runs.all {
                if (configLabel.isNotEmpty()) {
                    ideFolderName.set(configLabel)
                }
            }
        }
    }

    extensions.findByName("loom")?.let {
        configure<net.fabricmc.loom.api.LoomGradleExtensionAPI> {
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
}