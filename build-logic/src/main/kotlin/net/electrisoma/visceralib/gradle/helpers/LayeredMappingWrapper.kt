package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import javax.inject.Inject

open class LayeredMappingWrapper @Inject constructor(private val project: Project) {

    fun layered(configure: MappingBuilder.() -> Unit): Dependency {
        val loom = project.extensions.getByName("loom")

        val action = project.reflect.safeAction<Any> { internalBuilder ->
            val builderWrapper = MappingBuilder(internalBuilder)
            builderWrapper.configure()
        }

        val layeredMethod = loom.javaClass.methods.find {
            it.name == "layered" && it.parameterCount == 1
        } ?: throw NoSuchMethodException("Could not find 'layered' method on ${loom.javaClass.name}")

        layeredMethod.isAccessible = true

        return layeredMethod.invoke(loom, action) as Dependency
    }

    class MappingBuilder(private val internal: Any) {

        private fun invokeMethod(name: String, vararg args: Any?) {
            val method = internal.javaClass.methods.find {
                it.name == name && it.parameterCount == args.size
            } ?: throw NoSuchMethodException("Method $name with ${args.size} params not found on ${internal.javaClass.name}")

            method.isAccessible = true

            method.invoke(internal, *args)
        }

        fun officialMojangMappings() {
            invokeMethod("officialMojangMappings")
        }

        fun officialMojangMappings(action: Action<out Any>) {
            invokeMethod("officialMojangMappings", action)
        }

        fun parchment(notation: Any) {
            invokeMethod("parchment", notation.toString())
        }

        fun parchment(notation: Any, action: Action<out Any>) {
            invokeMethod("parchment", notation.toString(), action)
        }

        fun signatureFix(notation: Any) {
            invokeMethod("signatureFix", notation)
        }

        fun mappings(file: Any) {
            invokeMethod("mappings", file)
        }
    }
}