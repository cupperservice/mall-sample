import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
	kotlin("plugin.jpa") version "1.4.32"
}

group = "cupper.hj2"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation(platform("software.amazon.awssdk:bom:2.15.0"))
	implementation("software.amazon.awssdk:dynamodb")
	implementation("com.auth0:java-jwt:3.16.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("net.bytebuddy:byte-buddy:1.9.12")
	testImplementation("com.ninja-squad:springmockk:1.1.2")
//	testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
//	testImplementation("com.ninja-squad:springmockk:1.1.2")
	runtimeOnly("mysql:mysql-connector-java")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
