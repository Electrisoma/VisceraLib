package net.electrisoma.visceralib.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

// wet mop
class VisceraCleanupTasks : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            tasks.register("deepClean") {
                group = "cleanup"
                doLast {
                    allprojects.forEach { proj ->
                        val targets = listOf(
                            proj.layout.buildDirectory.asFile.get(),
                            proj.projectDir.resolve(".gradle")
                        )
                        targets.filter { it.exists() }.forEach { folder ->
                            println("Deleting: ${folder.absolutePath}")
                            folder.deleteRecursively()
                        }
                    }
                }
            }
        }
    }
}
