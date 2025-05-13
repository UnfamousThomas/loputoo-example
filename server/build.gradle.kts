plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.unfamousthomas"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:ebaa2bbf64")
}


tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "me.unfamousthomas.thesis.example.ExampleMain"
    }
}