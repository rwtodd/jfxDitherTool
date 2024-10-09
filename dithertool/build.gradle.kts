plugins {
   java
   application
   id("org.openjfx.javafxplugin") version ("0.1.0")
}

javafx {
    version = "23"
    modules = listOf("javafx.swing", "javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":ditherlib"))
    implementation("org.rwtodd:org.rwtodd.paldesign:1.0.0")
}

application {
    applicationName = "jfxdithertool"
    mainModule = "rwt.dithertool"
    mainClass = "rwt.dithertool.MainApp"
}
