import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

@Suppress("DSL_SCOPE_VIOLATION") plugins {
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


fun KotlinNativeTargetWithHostTests.setup() =
  binaries {
    executable { entryPoint = "com.fortysevendegrees.main" }
  }

println(System.getProperty("os.name"))

kotlin {
  if (System.getenv("CI").toBoolean()) {
    when(System.getProperty("os.name")) {
      "Mac OS X" -> macosX64 { setup() }
      "Linux" -> linuxX64 { setup() }
      null -> throw GradleException("null os.name.")
    }
  } else {
    linuxX64 { setup() }
    macosX64 { setup() }
    macosArm64 { setup() }
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