import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("com.diffplug.spotless")
}

plugins.apply("net.electrisoma.visceralib.gradle")

configure<SpotlessExtension> {
    java {
        target("src/*/*/net/electrisoma/visceralib/**/*.java")

        endWithNewline()
        trimTrailingWhitespace()
        removeUnusedImports()
        importOrder("net.electrisoma", "net.fabricmc", "net.neoforged", "net.minecraft", "com.mojang", "", "java", "javax")
        leadingSpacesToTabs(4)

        replaceRegex("newline after class-level opening", "^((?:public\\s+)?(?:class|interface|enum)\\b[^\\{]*\\{\\n)(?!\\n)", "$1\n")
        replaceRegex("newline after inner class-level opening", "^[\t ]{0,4}((?:public|protected|private|static|abstract|\\s)+?(?:class|interface)\\b[^\\{]*\\{\\n)(?!\\n)", "$1\n")
        replaceRegex("class-level javadoc indentation fix", "^\\*", " *")
        replaceRegex("method-level javadoc indentation fix", "\t\\*", "\t *")
    }
}

tasks.withType<JavaCompile> {
    dependsOn("spotlessApply")
}