@Suppress("DSL_SCOPE_VIOLATION") plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kover)
  alias(libs.plugins.kotlinx.serialization)
}

group = "org.jetbrains"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
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
      executable { entryPoint = "org.jetbrains.main" }
    }
  }
  
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.arrow.fx)
        implementation(libs.suspendapp)
        implementation(libs.suspendapp.ktor)
        implementation(libs.bundles.ktor.server)
      }
    }
    
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(libs.ktor.server.tests)
      }
    }
  }
}
