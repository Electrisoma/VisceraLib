package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven

open class MavenHelpers(private val project: Project) {

    fun modrinth(name: String, version: String) = "maven.modrinth:$name:$version"
    fun curseforge(name: String, pId: String, fId: String) = "curse.maven:$name-$pId:$fId"

    fun strictMaven(url: String, vararg coords: String) {
        project.repositories.exclusiveContent {
            forRepository { project.repositories.maven(url) }
            filter {
                coords.forEach {
                    if (":" in it) {
                        val (g, m) = it.split(":", limit = 2)
                        includeModule(g, m)
                    } else includeGroup(it)
                }
            }
        }
    }
}
