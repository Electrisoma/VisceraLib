package net.electrisoma.visceralib.gradle.helpers

import net.electrisoma.visceralib.gradle.extensions.mod
import org.gradle.api.Project
import net.fabricmc.loom.api.fabricapi.FabricApiExtension

open class FabricAPIBundle(private val project: Project) {

    fun embed(name: String) {
        val dep = api.module(name, fapiVersion)
        project.dependencies.add("modApi", dep)
        project.dependencies.add("include", dep)
    }

    fun runtime(name: String) {
        val dep = api.module(name, fapiVersion)
        project.dependencies.add("modRuntimeOnly", dep)
    }

    private val fapiVersion get() = "${project.mod.ver("fabric_api")}+${project.mod.mc}"
    private val api get() = project.extensions.getByName("fabricApi") as FabricApiExtension
}
