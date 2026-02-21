package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Project

open class VisceraLibProjectFinder(private val project: Project) {

    val common   get() = vProjects.filter { it.name.endsWith("-common") }
    val fabric   get() = vProjects.filter { it.name.endsWith("-fabric") }
    val neoforge get() = vProjects.filter { it.name.endsWith("-neoforge") }

    fun dependOn(projects: List<Project>): List<Project> {
        projects.forEach { project.evaluationDependsOn(it.path) }
        return projects
    }

    private val vProjects = project.rootProject.childProjects.values
        .filter { it.name.startsWith("visceralib-") && it != project }
}
