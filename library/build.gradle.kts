import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

val MODULE_PACKAGE_NAME: String by project
val MODULE_NAME: String by project
val MODULE_VERSION_NUMBER: String by project
val PUBLISH_NAME: String by project

group = MODULE_PACKAGE_NAME
version = MODULE_VERSION_NUMBER

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("io.github.luca992.multiplatform-swiftpackage") version "2.0.5-arm64"
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    kotlin("native.cocoapods")
    signing
    `maven-publish`
}

ktlint {
    version.set("0.43.0")
}

detekt {
    config = files("./custom-detekt-config.yml")
    buildUponDefaultConfig = true // preconfigure defaults
    source.setFrom(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/iosMain/kotlin"
    )
    autoCorrect = false
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = libs.versions.jvm.get()
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}

kotlin {
    js {
        browser { }
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()
    androidTarget {
        publishAllLibraryVariants()
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios()
    iosSimulatorArm64()
    cocoapods {
        ios.deploymentTarget = "11.0"
        framework {
            baseName = MODULE_NAME
            isStatic = true
        }
        pod("Analytics") {
            moduleName = "Segment"
            source = git("https://github.com/MyUNiDAYS/analytics-ios.git") {
                commit = "76e4d86780e56c566c0c99a05841b4a4c8905f72"
            }
        }
        pod("Segment-Firebase") {
            source = git("https://github.com/MyUNiDAYS/analytics-ios-integration-firebase.git") {
                commit = "a32e661f5871bd7d011890c046a24e0701e3dd34"
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting
        val jsTest by getting
        val androidMain by getting {
            dependencies {
                implementation("com.google.firebase:firebase-analytics:21.0.0")
                api("com.segment.analytics.android:analytics:4.10.4")
                api("com.appsflyer:segment-android-integration:6.5.2")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(testingLibs.junit)
            }
        }
        val iosMain by getting
        val iosSimulatorArm64Main by getting
        iosSimulatorArm64Main.dependsOn(iosMain)
        val iosTest by getting
        val iosSimulatorArm64Test by getting
        iosSimulatorArm64Test.dependsOn(iosTest)
    }
}

android {
    compileSdk = androidVersions.versions.compileSdk.get().toInt()
    buildToolsVersion = androidVersions.versions.buildToolsVersion.get()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    namespace = MODULE_PACKAGE_NAME
    defaultConfig {
        minSdk = androidVersions.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
}

fun SigningExtension.whenRequired(block: () -> Boolean) {
    setRequired(block)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.value("javadoc")
}

publishing {
    val OPEN_SOURCE_REPO: String by project
    val PUBLISH_DESCRIPTION: String by project
    val PUBLISH_URL: String by project
    val POM_DEVELOPER_ID: String by project
    val POM_DEVELOPER_NAME: String by project
    val POM_DEVELOPER_EMAIL: String by project
    val PUBLISH_SCM_URL: String by project
    val PUBLISH_SCM_CONNECTION: String by project
    val PUBLISH_SCM_DEVELOPERCONNECTION: String by project

    repositories {
        maven {
            url = uri(OPEN_SOURCE_REPO)

            credentials {
                username = System.getenv("sonatypeUsernameEnv")
                password = System.getenv("sonatypePasswordEnv")
            }
        }
    }

    publications.all {
        this as MavenPublication

        artifact(javadocJar)

        pom {
            name.set(PUBLISH_NAME)
            description.set(PUBLISH_DESCRIPTION)
            url.set(PUBLISH_URL)

            licenses {
                license {
                    name.set("MIT License")
                    url.set("http://opensource.org/licenses/MIT")
                }
            }

            developers {
                developer {
                    id.set(POM_DEVELOPER_ID)
                    name.set(POM_DEVELOPER_NAME)
                    email.set(POM_DEVELOPER_EMAIL)
                }
            }

            scm {
                url.set(PUBLISH_SCM_URL)
                connection.set(PUBLISH_SCM_CONNECTION)
                developerConnection.set(PUBLISH_SCM_DEVELOPERCONNECTION)
            }
        }
    }
}

multiplatformSwiftPackage {
    packageName(MODULE_NAME)
    swiftToolsVersion("5.4")
    targetPlatforms {
        iOS { v("13") }
    }
}

signing {
    whenRequired { gradle.taskGraph.hasTask("publish") }
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}
