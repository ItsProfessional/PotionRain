import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.yaml:snakeyaml:1.30")
    }
}

plugins {
    `java-library`
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.proferk"
version = "1.0"

val mcApiVersion: String by project

repositories {
    mavenCentral()
	maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

	compileOnly(group = "io.papermc.paper", name = "paper-api", version = "$mcApiVersion+")
}

val targetJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks {
    val configureShadowRelocation by registering(ConfigureShadowRelocation::class) {
        target = shadowJar.get()
        prefix = "${project.group}.${project.name.toLowerCase()}.libraries"
    }

    build {
        dependsOn(shadowJar).dependsOn(configureShadowRelocation)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        val props = mapOf(
            "version" to version,
            "apiVersion" to mcApiVersion,
            "kotlinVersion" to project.properties["kotlinVersion"]
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }

        doLast {
            val resourcesDir = sourceSets.main.get().output.resourcesDir
            val yamlDumpOptions = DumperOptions().also {
                it.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
                it.setPrettyFlow(true)
            }
            val yaml = Yaml(yamlDumpOptions)
            val pluginYml: Map<String, Any> = yaml.load(file("$resourcesDir/plugin.yml").inputStream())
            yaml.dump(pluginYml.filterKeys { it != "libraries" }, file("$resourcesDir/offline-plugin.yml").writer())
        }

        filteringCharset = Charsets.UTF_8.name()
    }

    val outputDir: String by project

    jar {
        exclude("offline-plugin.yml")

        destinationDirectory.set(file(outputDir))
    }

    shadowJar {
        minimize()
        archiveClassifier.set("offline")
        exclude("plugin.yml")
        rename("offline-plugin.yml", "plugin.yml")

//        destinationDirectory.set(file(outputDir))
    }
}
