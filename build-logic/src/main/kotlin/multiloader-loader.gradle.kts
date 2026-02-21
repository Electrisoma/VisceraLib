import net.electrisoma.visceralib.gradle.helpers.*

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

afterEvaluate {
    val base = listOfNotNull(
        mod.module.takeIf { it.isNotBlank() },
        mod.suffix.takeIf { it.isNotBlank() }
    ).joinToString("-")

    val specificPath = listOfNotNull(
        base.takeIf { it.isNotBlank() },
        mod.moduleVer.takeIf { it.isNotBlank() }
    ).joinToString("/")

    val configLabel = specificPath.ifBlank { mod.id }

    reflect.withExtension("neoForge") { neoForge ->
        val runs = reflect.invokeMethod(neoForge, "getRuns") as NamedDomainObjectContainer<*>
        runs.all(reflect.safeAction<Any> { runObj ->
            if (configLabel.isNotEmpty())
                reflect.setProperty(runObj, "ideFolderName", configLabel)
        })
    }

    reflect.withExtension("loom") { loom ->
        val runsAction = reflect.safeAction<Any> { runsContainer ->
            val allMethod = runsContainer.javaClass.getMethod("all", Action::class.java)
            allMethod.invoke(runsContainer, reflect.safeAction<Any> { runSettings ->
                if (configLabel.isNotEmpty())
                    reflect.setProperty(runSettings, "ideConfigFolder", configLabel)
                reflect.invokeMethod(runSettings, "ideConfigGenerated", true)
            })

            val getByName = runsContainer.javaClass.getMethod("getByName", String::class.java, Action::class.java)
            getByName.invoke(runsContainer, "client", reflect.safeAction<Any> { clientRun ->
                reflect.invokeMethod(clientRun, "programArgs", arrayOf("--username", "dev"))
            })
        }
        reflect.invokeMethod(loom, "runs", runsAction)
    }
}