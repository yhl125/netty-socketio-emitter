import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.4.21"
	maven
}

group = "com.github.yhl125"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib")

	implementation("org.msgpack:jackson-dataformat-msgpack:0.8.22")
	implementation("org.redisson:redisson:3.14.1")

	implementation("com.github.jitpack:gradle-simple:1.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
