plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`
    id("org.openjfx.javafxplugin") version "0.1.0"
}

base {
    archivesName = "org.rwtodd.dither"
}

javafx {
    version = "23"
    modules = listOf("javafx.graphics")
}

