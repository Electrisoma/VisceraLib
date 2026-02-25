package net.electrisoma.visceralib.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.withType
import org.gradle.api.tasks.javadoc.Javadoc
import net.electrisoma.visceralib.gradle.extensions.mod

class VisceraPublicationPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("maven-publish")

            tasks.withType<Jar>().configureEach {
                manifest {
                    attributes(mapOf(
                        "Fabric-Loom-Remap"      to "true",
                        "Specification-Title"    to mod.displayName,
                        "Specification-Vendor"   to mod.authors,
                        "Specification-Version"  to mod.version,
                        "Implementation-Title"   to project.name,
                        "Implementation-Version" to mod.version,
                        "Implementation-Vendor"  to mod.authors,
                        "Built-On-Minecraft"     to mod.mc
                    ))
                }
            }

            configure<PublishingExtension> {
                publications.create<MavenPublication>("mavenJava") {
                    from(components.getByName("java"))
                }
                repositories {
                    mavenLocal()
//                    maven {
//                       name = "GitHubPackages"
//                       url = project.uri("https://maven.pkg.github.com/electrisoma/VisceraLib")
//                       credentials {
//                           username = System.getenv("GITHUB_ACTOR") ?: props.local("ghpUsername")
//                           password = System.getenv("GITHUB_TOKEN") ?: props.local("ghpToken")
//                       }
//                    }
                }
            }
        }
    }
}
