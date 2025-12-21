import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import gradle.kotlin.dsl.accessors._f2a9aebd8c5798d32ebc7e5891a02610.implementation
import net.fabricmc.loom.api.fabricapi.FabricApiExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project

fun RepositoryHandler.strictMaven(url: String, vararg coords: String) {
    exclusiveContent {
        forRepository { maven(url) }
        filter {
            coords.forEach { coordinate ->
                if (":" in coordinate) {
                    val (group, module) = coordinate.split(":", limit = 2)
                    includeModule(group, module)
                } else {
                    includeGroup(coordinate)
                }
            }
        }
    }
}

fun DependencyHandlerScope.fapiModules(
    project: Project,
    vararg modules: String,
    version: String = project.currentMod.dep("fabric-api"),
    mcVersion: String = project.currentMod.mc,
    config: String = "modImplementation",
    include: Boolean = false
) {
    val factory = project.extensions.getByType<FabricApiExtension>()
    modules.forEach { name ->
        val dep = factory.module(name, "$version+$mcVersion")
        add(config, dep)
        if (include) {
            add("include", dep)
        }
    }
}

fun DependencyHandlerScope.listImplementation(projects: List<Project>) =
    projects.forEach { implementation(it) }

fun DependencyHandlerScope.listImplementation(projects: List<Project>, configuration: String) =
    projects.forEach { implementation(project(it.path, configuration)) }

fun Project.getMod(): ModData = ModData(this)
fun Project.prop(key: String): String? = findProperty(key)?.toString()

val Project.stonecutterBuild get() = extensions.getByType<StonecutterBuildExtension>()

val Project.common get() = requireNotNull(stonecutterBuild.node.sibling("common")) {
    "No common project for $project"
}

val Project.currentMod get() = this.getMod()
val Project.loader: String? get() = prop("loader")

@JvmInline
value class ModData(private val project: Project) {

    val id         : String get() = modProp("id")
    val name       : String get() = modProp("name")
    val module     : String get() = modProp("module")
    val moduleCaps : String get() = modProp("module_caps")
    val version    : String get() = modProp("version")
    val group      : String get() = modProp("group")
    val author     : String get() = modProp("author")
    val description: String get() = modProp("description")
    val license    : String get() = modProp("license")
    val github     : String get() = modProp("github")
    val mc         : String get() = depOrNull("minecraft")
        ?: project.stonecutterBuild.current.version

    fun propOrNull(key: String): String? = project.prop(key)
    fun prop(key: String)                = requireNotNull(propOrNull(key)) { "Missing '$key'" }

    fun modPropOrNull(key: String)       = project.prop("mod.$key")
    fun modProp(key: String)             = requireNotNull(modPropOrNull(key)) { "Missing 'mod.$key'" }

    fun depOrNull(key: String): String?  = project.prop("deps.$key")?.takeIf { it.isNotEmpty() && it != "" }
    fun dep(key: String)                 = requireNotNull(depOrNull(key)) { "Missing 'deps.$key'" }

    fun curseforge(name: String, projectId: String, fileId: String) = "curse.maven:$name-$projectId:$fileId"
    fun modrinth(name: String, version: String)                     = "maven.modrinth:$name:$version"
}