package net.electrisoma.visceralib.gradle

import net.electrisoma.visceralib.gradle.conventions.*
import net.electrisoma.visceralib.gradle.helpers.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class VisceraLibPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            extensions.create("mod", ModPropertyData::class.java, project)
            extensions.create("fapi", FabricAPIBundle::class.java, project)
            extensions.create("props", GradlePropertiesManager::class.java, project)
            extensions.create("repos", MavenHelpers::class.java, project)
            extensions.create("finder", VisceraLibProjectFinder::class.java, project)
            extensions.create("mapping", LayeredMappingWrapper::class.java, project)
            extensions.create("reflect", ReflectionHelper::class.java, project)

            plugins.apply(VisceraMetadataPlugin::class.java)
            plugins.apply(VisceraPublicationPlugin::class.java)
            plugins.apply(VisceraFormattingPlugin::class.java)
            plugins.apply(VisceraCleanupTasks::class.java)
            plugins.apply(VisceraConfigurationsPlugin::class.java)
            plugins.apply(VisceraRunConfigPlugin::class.java)
        }
    }
}
