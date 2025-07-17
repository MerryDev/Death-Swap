plugins {
    id("java")
}

group = "net.alphalightning"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
}