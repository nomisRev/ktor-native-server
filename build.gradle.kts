import io.github.nomisrev.composeAroundTest

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
  databases {
    create("NativePostgres") {
      packageName.set("com.fortysevendegrees.sqldelight")
      dialect(libs.postgres.native.dialect.get())
    }
  }
  linkSqlite.set(false)
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
