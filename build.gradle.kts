plugins {
    id("java-library")
}

group = "de.finanzberg"
version = "1.0.0"


java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}