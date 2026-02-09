import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.fabricapi.FabricApiExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project

val Project.mod get() = ModData(this)

val Project.stonecutterBuild get() = extensions.getByType<StonecutterBuildExtension>()

val Project.loader: String? get() = findProperty("loader")?.toString()
val Project.module: String? get() {
    val name = findProperty("module")?.toString() ?: return null
    val suffix = findProperty("suffix")?.toString()?.takeIf { it.isNotBlank() }
    val ver = findProperty("module_version")?.toString()?.takeIf { it.isNotBlank() }
    return listOfNotNull(name, suffix, ver).joinToString("_")
}

val Project.commonNode get() = requireNotNull(stonecutterBuild.node.sibling("common")) {
    "No common project found for $name"
}

@JvmInline
value class ModData(private val project: Project) {

    val id: String           get() = prop("mod.id")
    val name: String         get() = prop("mod.name")
    val version: String      get() = prop("mod.version")
    val group: String        get() = prop("mod.group")
    val authors: String      get() = prop("mod.authors")
    val contributors: String get() = prop("mod.contributors")
    val description: String  get() = prop("mod.description")
    val license: String      get() = prop("mod.license")
    val github: String       get() = prop("mod.github")

    val mc: String get() = depOrNull("minecraft") ?: project.stonecutterBuild.current.version

    fun dep(key: String): String = requireNotNull(depOrNull(key)) { "Missing 'deps.$key' in gradle.properties" }

    fun depOrNull(key: String): String? =
        project.findProperty("deps.$key")?.toString()?.takeIf { it.isNotBlank() }

    private fun prop(key: String): String =
        project.findProperty(key)?.toString() ?: throw GradleException("Property '$key' is missing!")
}

private fun fapiVersion(project: Project) =
    "${project.mod.dep("fabric_api")}+${project.mod.mc}"

fun DependencyHandlerScope.minecraft(project: Project) =
    add("minecraft", "com.mojang:minecraft:${project.mod.mc}")

fun DependencyHandlerScope.fabricLoader(project: Project) =
    add("modImplementation", "net.fabricmc:fabric-loader:${project.mod.dep("fabric_loader")}")

fun DependencyHandlerScope.embedFapi(project: Project, name: String) {
    val factory = project.extensions.getByType<FabricApiExtension>()
    val module = factory.module(name, fapiVersion(project))
    add("modApi", module)
    add("include", module)
}

fun DependencyHandlerScope.runtimeFapi(project: Project, name: String) {
    val factory = project.extensions.getByType<FabricApiExtension>()
    add("modRuntimeOnly", factory.module(name, fapiVersion(project)))
}

fun DependencyHandlerScope.listImplementation(projects: List<Project>) {
    projects.forEach { proj ->
        add("implementation", proj).apply {
            if (this is ProjectDependency) isTransitive = false
        }
    }
}

fun DependencyHandlerScope.listImplementation(projects: List<Project>, configuration: String) {
    projects.forEach { proj ->
        add("implementation", project(proj.path, configuration)).apply {
            if (this is ProjectDependency) isTransitive = false
        }
    }
}

fun DependencyHandlerScope.listCompile(projects: List<Project>) {
    projects.forEach { proj ->
        add("compileOnly", proj)
    }
}

fun DependencyHandlerScope.listModCompile(projects: List<Project>) {
    projects.forEach { proj ->
        add("modCompileOnly", proj)
    }
}

@Suppress("UnstableApiUsage")
fun layeredMappings(project: Project): Dependency = project.extensions.getByType<LoomGradleExtensionAPI>().layered {
    officialMojangMappings()
    project.mod.depOrNull("parchment")?.let { version ->
        parchment("org.parchmentmc.data:parchment-${project.mod.mc}:$version@zip")
    }
}

fun modrinth(name: String, version: String) = "maven.modrinth:$name:$version"
fun curseforge(name: String, projectId: String, fileId: String) = "curse.maven:$name-$projectId:$fileId"

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