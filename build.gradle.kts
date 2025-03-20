plugins {
    id("java")
    id("idea")
}

group = "io.github.wxrlock.conversationframework"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.elmakers.com/repository/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}