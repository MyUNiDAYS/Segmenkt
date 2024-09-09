import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.ByteArrayOutputStream

plugins {
    alias(testingLibs.plugins.kover)
}

kover {
    engine.set(kotlinx.kover.api.DefaultIntellijEngine)
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:11.0.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.10")
        classpath(libs.android.build.tools)
        classpath(libs.gradle.plugin)
        classpath(testingLibs.detekt)
        classpath(testingLibs.kotest.plugin)
    }
}

allprojects {
    val properties = gradleLocalProperties(project.rootDir)

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            val token =
                System.getenv("CODEARTIFACT_AUTH_TOKEN").takeUnless { it.isNullOrBlank() } ?:
                runCatching {
                    // Optional: If you have multiple profiles, you can specify which one to use
                    val profile =
                        System.getenv("CI")?.let { "--profile ci" } ?:
                        properties.getProperty("aws.profile")?.let {"--profile $it" }
                        ?: ""
                    "aws codeartifact get-authorization-token --domain unidays-maven --domain-owner 899021384373 --region eu-west-1 --query authorizationToken --output text $profile".runCommand()
                }.onFailure {
                    println("Error: Failed to get codeartifact authorisation token, do you have a valid sso token?")
                }.getOrNull()
            url = uri("https://unidays-maven-899021384373.d.codeartifact.eu-west-1.amazonaws.com/maven/unidays-maven/")
            credentials {
                username = "aws"
                password = token.toString()
            }
        }
    }
}

fun String.runCommand(currentWorkingDir: File = file("./")): String {
    val byteOut = ByteArrayOutputStream()
    project.exec {
        workingDir = currentWorkingDir
        commandLine = this@runCommand.split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}