import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import dev.kikugie.stonecutter.controller.StonecutterControllerExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

fun Project.getMod(): ModData = ModData(this)
fun Project.prop(key: String): String? = findProperty(key)?.toString()

val Project.stonecutterBuild get() = extensions.getByType<StonecutterBuildExtension>()
val Project.stonecutterController get() = extensions.getByType<StonecutterControllerExtension>()

val Project.common get() = requireNotNull(stonecutterBuild.node.sibling("common")) {
    "No common project for $project"
}
val Project.commonProject get() = project.parent!!
val Project.commonMod get() = commonProject.getMod()

val Project.currentMod get() = this.getMod()
val Project.loader: String? get() = prop("loader")

@JvmInline
value class ModData(private val project: Project) {

    val id: String get() = project.commonMod.modProp("id")
    val name: String get() = project.commonMod.modProp("name")
    val version: String get() = project.commonMod.modProp("version")
    val group: String get() = project.commonMod.modProp("group")
    val author: String get() = project.commonMod.modProp("author")
    val description: String get() = project.commonMod.modProp("description")
    val license: String get() = project.commonMod.modProp("license")
    val github: String get() = project.commonMod.modProp("github")

    val mc: String get() = depOrNull("minecraft")
        ?: project.commonMod.depOrNull("minecraft")
        ?: project.stonecutterBuild.current.version

    fun propOrNull(key: String): String? = project.prop(key)
    fun prop(key: String) = requireNotNull(propOrNull(key)) { "Missing '$key'" }
    fun modPropOrNull(key: String) = project.prop("mod.$key")
    fun modProp(key: String) = requireNotNull(modPropOrNull(key)) { "Missing 'mod.$key'" }
    fun depOrNull(key: String): String? = project.prop("deps.$key")?.takeIf { it.isNotEmpty() && it != "" }
    fun dep(key: String) = requireNotNull(depOrNull(key)) { "Missing 'deps.$key'" }
    fun modrinth(name: String, version:String) = "maven.modrinth:$name:$version"
}