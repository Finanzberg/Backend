import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import java.io.ByteArrayOutputStream
import java.util.stream.Stream
import kotlin.streams.asStream

plugins {
    id("java-library")
    id("application")

    alias(libs.plugins.shadow)
}

group = "de.finanzberg"
version = "1.0.0"

application {
    mainClass.set("de.finanzberg.backend.main.FinanzbergMain")
}

repositories{
    mavenCentral()
}

dependencies{
    implementation(libs.bundles.log4j)
    implementation(libs.terminalconsoleappender)
    implementation("org.jetbrains:annotations:24.0.0")
    runtimeOnly(libs.jline.jansi)
    runtimeOnly(libs.disruptor)

    api(libs.google.gson)
    api(libs.configurate.yaml)

    api(libs.mariaDB)
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks{
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-parameters")
    }

    jar {
        manifest.attributes(
                "Implementation-Vendor" to "Nik, pianoman911, xlennnix",
                "Implementation-Version" to project.version,
                "Implementation-Title" to project.name,

                "Git-Commit" to gitRevParse("short"),
                "Git-Branch" to gitRevParse("abbrev-ref"),
                "Timestamp" to System.currentTimeMillis().toString(),
        )
    }

    shadowJar {
        transform(Log4j2PluginsCacheFileTransformer::class.java)
    }

    assemble {
        dependsOn(shadowJar)
    }
}

fun gitRevParse(arg: String): String {
    return gitCommand(rootDir, "rev-parse", "--$arg", "HEAD")
}

fun gitCommand(workDir: File, vararg args: String): String {
    val out = ByteArrayOutputStream()
    rootProject.exec {
        commandLine(Stream.concat(Stream.of("git"), args.asSequence().asStream()).toList())
        workingDir = workDir

        standardOutput = out
        errorOutput = out
    }
    return out.toString().trim()
}