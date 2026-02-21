package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Project
import java.util.*

open class GradlePropertiesManager(private val project: Project) {

    fun load(path: String): Properties = Properties().apply {
        project.file(path).takeIf { it.exists() }?.inputStream()?.use(::load)
    }

    fun fromRoot(path: String): Properties = Properties().apply {
        project.rootProject.file(path).takeIf { it.exists() }?.inputStream()?.use(::load)
    }

    fun getFileProp(path: String, key: String): String? = load(path).getProperty(key)

    fun local(key: String): String? = fromRoot("local.properties").getProperty(key)

    val loader: String? get() = getFileProp("loader.properties", "loader")
}
