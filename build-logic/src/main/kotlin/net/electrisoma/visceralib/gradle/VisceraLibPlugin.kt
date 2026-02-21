package net.electrisoma.visceralib.gradle

import net.electrisoma.visceralib.gradle.helpers.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class VisceraLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("mod", ModPropertyData::class.java, project)
        project.extensions.create("fapi", FabricAPIBundle::class.java, project)
        project.extensions.create("props", GradlePropertiesManager::class.java, project)
        project.extensions.create("repos", MavenHelpers::class.java, project)
        project.extensions.create("finder", VisceraLibProjectFinder::class.java, project)
        project.extensions.create("mapping", LayeredMappingWrapper::class.java, project)
        project.extensions.create("reflect", ReflectionHelper::class.java, project)
    }
}