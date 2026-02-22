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
                        val dir = proj.projectDir

                        val targetFolders = listOf(
                            proj.layout.buildDirectory.asFile.getOrNull(),
                            dir.resolve(".gradle"),
                            dir.resolve("run"),
                            dir.resolve("out")
                        )

                        val targetFiles = dir.listFiles()?.filter { file ->
                            file.isFile && (file.name.endsWith(".launch"))
                        } ?: emptyList()

                        targetFolders.filterNotNull().filter { it.exists() }.forEach { folder ->
                            println("Cleaning folder: ${folder.absolutePath}")
                            folder.deleteRecursively()
                        }

                        targetFiles.forEach { file ->
                            println("Cleaning file: ${file.absolutePath}")
                            file.delete()
                        }
                    }
                }
            }
        }
    }
}
