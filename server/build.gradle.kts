/*
 *  Copyright 2026 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.openapitools.generator.gradle.plugin.tasks.ValidateTask

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.openapi)
}

group = project.property("basePackageName") as String
version = project.property("version") as String

fun requiredProjectProperty(name: String): String =
    requireNotNull(project.findProperty(name)) {
        "Required Gradle project property '$name' is not defined."
    }.toString()

fun booleanProjectProperty(name: String): Boolean =
    requiredProjectProperty(name).toBooleanStrict()

fun indexedProjectProperties(indexName: String, prefix: String): Map<String, String> =
    requiredProjectProperty(indexName)
        .split(',')
        .map(String::trim)
        .filter(String::isNotEmpty)
        .associateWith { requiredProjectProperty("$prefix$it") }

val openApiInputSpec = requiredProjectProperty("openApi.inputSpec")
val openApiOutputDirectory = requiredProjectProperty("openApi.outputDirectory")
val openApiSourceFolder = requiredProjectProperty("openApi.sourceFolder")
val openApiConfigPrefix = "openApi.config."
val openApiGlobalPrefix = "openApi.global."
val openApiConfigOptions =
    indexedProjectProperties("openApi.configOptions", openApiConfigPrefix)
        .plus("sourceFolder" to openApiSourceFolder)
val openApiGlobalProperties =
    indexedProjectProperties("openApi.globalProperties", openApiGlobalPrefix)
val generatedOpenApiRoot = layout.buildDirectory.dir(openApiOutputDirectory)
val generatedOpenApiKotlin = generatedOpenApiRoot.map { it.dir(openApiSourceFolder) }

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.valueOf("JVM_${libs.versions.java.get()}")
    }
    sourceSets {
        named("main") {
            kotlin.srcDir(generatedOpenApiKotlin)
        }
    }
}

dependencies {

    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.kotlin)

    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.oauth2.resource.server)
//    testImplementation("org.springframework.security:spring-security-test")

    developmentOnly(libs.spring.boot.devtools)

    runtimeOnly(libs.h2)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)

}

tasks.withType<Test> {
    useJUnitPlatform()
}

val validateGraffitiApi = tasks.register<ValidateTask>("validateGraffitiApi") {
    group = "openapi"
    description = "Validates the Graffiti OpenAPI specification."
    inputSpec.set(layout.projectDirectory.file(openApiInputSpec).asFile.absolutePath)
}

val generateGraffitiApi = tasks.register<GenerateTask>("generateGraffitiApi") {
    group = "openapi"
    description = "Generates Graffiti Spring MVC interfaces and DTOs."
    dependsOn(validateGraffitiApi)

    generatorName.set(requiredProjectProperty("openApi.generatorName"))
    inputSpec.set(layout.projectDirectory.file(openApiInputSpec).asFile.absolutePath)
    outputDir.set(generatedOpenApiRoot)
    apiPackage.set(requiredProjectProperty("openApi.apiPackage"))
    modelPackage.set(requiredProjectProperty("openApi.modelPackage"))
    configOptions.set(openApiConfigOptions)
    globalProperties.set(openApiGlobalProperties)
    cleanupOutput.set(booleanProjectProperty("openApi.cleanupOutput"))

    generateApiTests.set(booleanProjectProperty("openApi.generateApiTests"))
    generateModelTests.set(booleanProjectProperty("openApi.generateModelTests"))
    generateApiDocumentation.set(
        booleanProjectProperty("openApi.generateApiDocumentation")
    )
    generateModelDocumentation.set(
        booleanProjectProperty("openApi.generateModelDocumentation")
    )
}

tasks.named("compileKotlin") {
    dependsOn(generateGraffitiApi)
}
