import org.gradle.accessors.dm.LibrariesForLibs
import net.electrisoma.visceralib.gradle.helpers.*

val libs = the<LibrariesForLibs>()

plugins {
    `java-library`
    idea
    id("common-formatting")
    id("common-metadata")
    id("common-publication")
    id("common-tasks")
}

plugins.apply("net.electrisoma.visceralib.gradle")

group = "${mod.group}.${mod.id}"
version = "${mod.version}+mc${mod.mc}"
base.archivesName = "${mod.moduleBase}-${props.loader}"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(mod.java))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    repos.strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data")
    repos.strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    repos.strictMaven("https://cursemaven.com", "curse.maven")
    repos.strictMaven("https://repo.spongepowered.org/repository/maven-public", "org.spongepowered")
    repos.strictMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
}

dependencies {
    annotationProcessor(libs.autoservice)
    compileOnly(libs.autoservice.annotations)
    api(libs.findbugs)
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:unchecked")
    }
}