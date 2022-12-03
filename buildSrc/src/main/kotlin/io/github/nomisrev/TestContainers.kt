package io.github.nomisrev

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

fun Project.composeAroundTest(
  vararg files: File = arrayOf(file("docker-compose.yml")),
  configure: DockerComposeContainer<Nothing>.() -> Unit = {}
) {
  val container: DockerComposeContainer<Nothing> =
    DockerComposeContainer<Nothing>(*files).apply(configure)
  
  val composeUp = task("composeUp") {
    doLast { container.start() }
  }
  
  val composeStop = task("composeStop") {
    doLast { container.stop() }
  }
  
  afterEvaluate {
    tasks.withType<KotlinNativeTest>().forEach {
      it.dependsOn(composeUp)
      it.finalizedBy(composeStop)
      composeUp.shouldRunAfter(it.taskDependencies)
    }
  }
}
