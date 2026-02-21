import net.electrisoma.visceralib.gradle.helpers.mod

plugins {
    `maven-publish`
}

plugins.apply("net.electrisoma.visceralib.gradle")

tasks {
    withType<Jar> {
        from(rootProject.file("LICENSE")) {
            rename { "$it-${mod.moduleBase}" }
        }

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

    named<Javadoc>("javadoc") {
        exclude("**/package-info.java")
        exclude("net/electrisoma/visceralib/impl/**")
        exclude("net/electrisoma/visceralib/mixin/**")
        exclude("net/electrisoma/visceralib/platform/**")
    }
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
    }
    repositories {
        mavenLocal()
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/electrisoma/VisceraLib")
//            credentials {
//                username = System.getenv("GITHUB_ACTOR") ?: props.local("ghpUsername")
//                password = System.getenv("GITHUB_TOKEN") ?: props.local("ghpToken")
//            }
//        }
    }
}