import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

val Project.mod get() = ModData(this)

val Project.loader: String? get() = findProperty("loader")?.toString()
val Project.module: String? get() {
    val name = findProperty("module")?.toString() ?: return null
    val suffix = findProperty("suffix")?.toString()?.takeIf { it.isNotBlank() }
    val ver = findProperty("module_version")?.toString()?.takeIf { it.isNotBlank() }
    return listOfNotNull(name, suffix, ver).joinToString("_")
}

class ModData(private val project: Project) {

    val id: String           get() = prop("mod_id")
    val name: String         get() = prop("mod_name")
    val version: String      get() = prop("mod_version")
    val group: String        get() = prop("mod_group")
    val module: String get() = project.findProperty("module")?.toString() ?: ""

    val authors: String      get() = prop("mod_authors")
    val contributors: String get() = prop("mod_contributors")
    val description: String  get() = prop("mod_description")
    val license: String      get() = prop("mod_license")
    val github: String       get() = prop("mod_github")

    val mc: String           get() = prop("minecraft_version")
    val minMc: String        get() = prop("min_minecraft_version")
    val java: String         get() = prop("java_version")

    fun ver(key: String): String = project.providers.gradleProperty(key).getOrElse("")
        .takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("Version key '$key' is missing in gradle.properties")

    private fun prop(key: String): String = project.providers.gradleProperty(key).getOrElse("")
        .takeIf { it.isNotBlank() }
        ?: throw IllegalStateException("Property '$key' is missing in gradle.properties")
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