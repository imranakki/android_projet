plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.spotless) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            target("src/**/*.java")
            googleJavaFormat("1.25.2").aosp().reflowLongStrings()
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }

        format("xml") {
            target("src/**/*.xml")
            trimTrailingWhitespace()
            endWithNewline()
        }

        format("gradle") {
            target("*.gradle.kts")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}