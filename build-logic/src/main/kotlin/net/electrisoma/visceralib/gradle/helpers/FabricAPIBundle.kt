package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Project
import org.gradle.api.provider.Property

open class FabricAPIBundle(private val project: Project) {

    private val fapiVersion: String get() {
        val mc = getModProperty("ver", "minecraft_version")
        val ver = getModProperty("ver", "fabric_api")
        return "$ver+$mc"
    }

    fun embed(name: String) {
        project.dependencies.add("modApi", getModule(name))
        project.dependencies.add("include", getModule(name))
    }

    fun runtime(name: String) {
        project.dependencies.add("modRuntimeOnly", getModule(name))
    }

    private fun getModule(name: String): Any {
        val fabricApiExt = project.extensions.getByName("fabricApi")
        val moduleMethod = fabricApiExt.javaClass.methods.find {
            it.name == "module" && it.parameterCount == 2
        } ?: throw NoSuchMethodException("Could not find 'module' method on fabricApi extension")

        return moduleMethod.invoke(fabricApiExt, name, fapiVersion)
            ?: throw IllegalStateException("Fabric API module $name returned null for version $fapiVersion")
    }

    private fun getModProperty(methodName: String, vararg args: Any): String {
        val modExt = project.extensions.getByName("mod")

        val method = modExt.javaClass.methods.find {
            it.name == methodName && it.parameterCount == args.size
        }

        return when (val result = method?.invoke(modExt, *args)) {
            is Property<*> -> result.get().toString()
            null -> ""
            else -> result.toString()
        }
    }
}
