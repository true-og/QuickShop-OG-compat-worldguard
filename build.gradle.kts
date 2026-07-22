plugins {
    id("java-library")
    id("com.diffplug.spotless") version "8.1.0"
    id("com.gradleup.shadow") version "8.3.9"
    id("checkstyle")
    eclipse
    kotlin("jvm") version "2.1.21"
}

group = "com.ghostchu.quickshop.compatibility"

version = "5.9"

val quickShopVersion = "5.2.0.7"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.GRAAL_VM)
    }
}

kotlin { jvmToolchain(17) }

sourceSets.named("main") {
    java.setSrcDirs(listOf("compatibility/worldguard/src/main/java"))
    resources.setSrcDirs(listOf("compatibility/worldguard/src/main/resources"))
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.ghostchu:quickshop-api:$quickShopVersion")
    compileOnly("com.ghostchu:quickshop-bukkit:$quickShopVersion:shaded")
    compileOnly("com.ghostchu:simplereloadlib:1.1.2")
    implementation("com.ghostchu.quickshop.compatibility:common:$quickShopVersion") { isTransitive = false }
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
    compileOnly("com.github.juliomarcopineda:jdbc-stream:0.1.1")
    compileOnly("one.util:streamex:0.8.2")
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filesMatching("plugin.yml") { expand(props) }
    from("LICENSE") { into("/") }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveBaseName.set(rootProject.name)
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    archiveClassifier.set("")
    minimize()
}

tasks.jar { enabled = false }

tasks.withType<JavaCompile>().configureEach {
    dependsOn("spotlessApply")
    options.compilerArgs.addAll(listOf("-parameters", "-Xlint:deprecation"))
    options.encoding = "UTF-8"
    options.isFork = true
}

spotless {
    java {
        eclipse().configFile("config/formatter/eclipse-java-formatter.xml")
        leadingTabsToSpaces()
        removeUnusedImports()
    }
    kotlinGradle {
        ktfmt().kotlinlangStyle().configure { it.setMaxWidth(120) }
        target("build.gradle.kts", "settings.gradle.kts")
    }
}

checkstyle {
    toolVersion = "10.18.1"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = true
    isShowViolations = true
}

tasks.build { dependsOn(tasks.shadowJar) }

eclipse.project.name = rootProject.name
