plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.8.10"
    application
    id("com.github.weave-mc.weave-gradle") version "bcf6ab0279"
}

group = "me.lunathefox20"
version = "1.0"

minecraft.version("1.8.9")

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
    mavenCentral()
}

dependencies {
    compileOnly("com.github.weave-mc:weave-loader:b40f2ae")

    compileOnly("org.spongepowered:mixin:0.8.5")
    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.apache.logging.log4j:log4j-api:2.16.0")
    implementation("org.apache.logging.log4j:log4j-core:2.16.0")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(8)
}