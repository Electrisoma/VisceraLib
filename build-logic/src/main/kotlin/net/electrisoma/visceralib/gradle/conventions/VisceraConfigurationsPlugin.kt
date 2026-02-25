package net.electrisoma.visceralib.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

class VisceraConfigurationsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            configurations {
                plugins.withId("net.neoforged.moddev") {
                    val accessTransformersApi = configurations.maybeCreate("accessTransformersApi")
                    val interfaceInjectionDataApi = configurations.maybeCreate("interfaceInjectionDataApi")
                    val mdgLocalRuntime = configurations.maybeCreate("mdgLocalRuntime")

                    named("accessTransformers").configure { extendsFrom(accessTransformersApi) }
                    named("accessTransformersElements").configure { extendsFrom(accessTransformersApi) }

                    named("interfaceInjectionData").configure { extendsFrom(interfaceInjectionDataApi) }
                    named("interfaceInjectionDataElements").configure { extendsFrom(interfaceInjectionDataApi) }

                    named("runtimeClasspath").configure { extendsFrom(mdgLocalRuntime) }
                }
            }
        }
    }
}
