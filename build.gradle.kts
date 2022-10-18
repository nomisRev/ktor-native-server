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
  mavenLocal()
}

setupDetekt()

sqldelight {
  database("NativePostgres") {
    dialect(libs.postgres.native.dialect.get())
    packageName = "com.fortysevendegrees.sqldelight"
    // deriveSchemaFromMigrations = true
  }
  linkSqlite = false
}


fun KotlinNativeTargetWithHostTests.setup() =
  binaries {
    executable { entryPoint = "com.fortysevendegrees.main" }
  }


kotlin {
  when (System.getProperty("os.name")) {
    "Linux" -> linuxX64 { setup() }
    "Mac OS X" -> when (System.getProperty("os.arch")) {
      "aarch64" -> macosArm64 { setup() }
      else -> macosX64 { setup() }
    }
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
        implementation(kotlin("test"))
      }
    }
  }
}