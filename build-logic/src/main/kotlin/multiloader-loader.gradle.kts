plugins {
    id("multiloader-common")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = true
}
val commonResources: Configuration by configurations.creating {
    isCanBeResolved = true
}

dependencies {
    val commonProject = rootProject.childProjects["${project.name.substringBeforeLast("-")}-common"]!!
    compileOnly(project(path = commonProject.path))
    commonJava(project(path = commonProject.path, configuration = "commonJava"))
    commonResources(project(path = commonProject.path, configuration = "commonResources"))
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    withType<Jar> {
        exclude("accesswideners", "accesswideners/**")
    }

    named<JavaCompile>("compileJava") {
        dependsOn(commonJava)
        source(commonJava)
    }

    named<ProcessResources>("processResources") {
        dependsOn(commonResources)
        from(commonResources)
    }
}