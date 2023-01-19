plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.citizensnpcs.co/repo")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    compileOnly("org.spigotmc:spigot:1.19.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.18.2-R0.1-SNAPSHOT")

    compileOnly("net.citizensnpcs:citizensapi:2.0.30-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly(files("libs/InteractionVisualizer.jar"))
    compileOnly(files("libs/TrChat-2.0.0-RC-6.jar"))
    compileOnly("com.mojang:authlib:1.5.25")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

group = "com.github.summericebearstudio"
version = "3.0.0"
description = "NereusOpusBase"

tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}