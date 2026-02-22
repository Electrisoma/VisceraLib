package net.electrisoma.visceralib.gradle.extensions

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.accessTransformersApi(notation: Any): Dependency? = add("accessTransformersApi", notation)
fun DependencyHandler.interfaceInjectionDataApi(notation: Any): Dependency? = add("interfaceInjectionDataApi", notation)
fun DependencyHandler.mdgLocalRuntime(notation: Any): Dependency? = add("mdgLocalRuntime", notation)
