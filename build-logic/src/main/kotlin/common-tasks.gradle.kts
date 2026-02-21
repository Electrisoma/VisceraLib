plugins {
    id("com.dorongold.task-tree")
}

plugins.apply("net.electrisoma.visceralib.gradle")

tasks {
    register("deepClean") {
        group = "cleanup"
        doLast {
            allprojects.forEach { proj ->
                val targets = listOf(
                    proj.layout.buildDirectory.asFile.get(),
                    proj.projectDir.resolve(".gradle")
                )

                targets.forEach { folder ->
                    if (folder.exists()) {
                        println("Deleting: ${folder.absolutePath}")
                        folder.deleteRecursively()
                    }
                }
            }
        }
    }
}