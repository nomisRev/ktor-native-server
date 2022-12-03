import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

@Suppress("DSL_SCOPE_VIOLATION") plugins {
  id("com.avast.gradle.docker-compose") version "0.16.11"
  alias(libs.plugins.kotest.multiplatform)
  id(libs.plugins.kotlin.multiplatform.pluginId)
  id(libs.plugins.detekt.pluginId)
  alias(libs.plugins.kover)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.sqldelight)
}

group = "com.fortysevendegrees"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

setupDetekt()

sqldelight {
  database("NativePostgres") {
    dialect(libs.postgres.native.dialect.get())
    packageName = "com.fortysevendegrees.sqldelight"
  }
  linkSqlite = false
}

kotlin {
  when (val os = System.getProperty("os.name")) {
    "Linux" -> linuxX64()
    "Mac OS X" -> when (System.getProperty("os.arch")) {
      "aarch64" -> macosArm64()
      else -> macosX64()
    }
    
    else -> throw GradleException("Host OS ($os) is not supported for this project.")
  }.apply {
    binaries {
      executable { entryPoint = "com.fortysevendegrees.main" }
    }
  }
  
  tasks.withType<KotlinNativeTest>().forEach {
    dockerCompose.isRequiredBy(it)
  }
  
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.arrow.fx)
        implementation(libs.suspendapp)
        implementation(libs.bundles.ktor.server)
        implementation(libs.postgres.native.driver)
      }
    }
    
    val commonTest by getting {
      dependencies {
        implementation(libs.bundles.kotest)
        implementation(libs.ktor.server.tests)
      }
    }
  }
}
