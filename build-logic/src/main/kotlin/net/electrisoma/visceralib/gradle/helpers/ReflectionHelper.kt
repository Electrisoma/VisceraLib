package net.electrisoma.visceralib.gradle.helpers

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property

open class ReflectionHelper(private val project: Project) {

    fun withExtension(name: String, block: (Any) -> Unit) {
        project.extensions.findByName(name)?.let { block(it) }
    }

    fun invokeMethod(target: Any, name: String, vararg args: Any?): Any? {
        val method = target.javaClass.methods.find { m ->
            m.name == name &&
                    m.parameterCount == args.size &&
                    m.parameterTypes.zip(args).all { (paramType, arg) ->
                        arg == null || paramType.isAssignableFrom(arg.javaClass) ||
                                (paramType.isInterface && arg is org.gradle.api.Action<*>)
                    }
        } ?: target.javaClass.methods.find { it.name == name && it.parameterCount == args.size }
        ?: throw NoSuchMethodException("No matching method $name found on ${target.javaClass}")

        method.isAccessible = true
        return method.invoke(target, *args)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> setProperty(target: Any, propertyName: String, value: T?) {
        val getterName = "get${propertyName.replaceFirstChar { it.uppercase() }}"
        val property = invokeMethod(target, getterName) as? Property<T>
        property?.set(value)
    }

    fun <T : Any> safeAction(block: (T) -> Unit): Action<T> {
        return object : Action<T> {
            override fun execute(t: T) = block(t)
        }
    }
}
