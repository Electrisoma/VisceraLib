package net.electrisoma.visceralib.gradle.conventions

import net.electrisoma.visceralib.gradle.extensions.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

class VisceraRunConfigPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.afterEvaluate {

            if (project.name.endsWith("-common") || project.name == "common")
                return@afterEvaluate

            val base = listOfNotNull(
                mod.module.takeIf { it.isNotBlank() },
                mod.suffix.takeIf { it.isNotBlank() }
            ).joinToString("-")

            val specificPath = listOfNotNull(
                base.takeIf { it.isNotBlank() },
                mod.moduleVer.takeIf { it.isNotBlank() }
            ).joinToString("/")

            val configLabel = specificPath.ifBlank { mod.id }

            if (configLabel.isEmpty()) return@afterEvaluate

            plugins.withId("net.neoforged.moddev") {
                configure<net.neoforged.moddevgradle.dsl.NeoForgeExtension> {
                    runs.all {
                        ideFolderName.set(configLabel)
                    }
                }
            }
            plugins.withId("net.fabricmc.fabric-loom-remap") {
                configure<net.fabricmc.loom.api.LoomGradleExtensionAPI> {
                    runs {
                        all {
                            ideConfigFolder.set(configLabel)
                            ideConfigGenerated(true)
                        }
                        getByName("client") {
                            programArgs("--username", "dev")
                        }
                    }
                }
            }
        }
    }
}
