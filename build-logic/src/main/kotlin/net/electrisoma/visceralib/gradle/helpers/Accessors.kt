package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

val Project.mod:     ModPropertyData get() = the()
val Project.props:   GradlePropertiesManager get() = the()
val Project.fapi:    FabricAPIBundle get() = the()
val Project.repos:   MavenHelpers get() = the()
val Project.finder:  VisceraLibProjectFinder get() = the()
val Project.mapping: LayeredMappingWrapper get() = the()
val Project.reflect: ReflectionHelper get() = the()