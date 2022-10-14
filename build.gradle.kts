import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("multiplatform") version "1.7.20"
  id("app.cash.sqldelight") version "2.0.0-alpha04"
}

group = "com.fortysevendegrees"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

sqldelight {
  database("NativePostgres") {
    dialect("app.softwork:postgres-native-sqldelight-dialect:0.0.2")
    packageName = "com.fortysevendegrees.sqldelight"
  }
  linkSqlite = false
}

kotlin {
  linuxX64 {
    binaries {
      executable {
        entryPoint = "com.fortysevendegrees.main"
      }
    }
  }
  
  macosX64 {
    binaries {
      executable {
        entryPoint = "com.fortysevendegrees.main"
      }
    }
  }
  
  macosArm64 {
    binaries {
      executable {
        entryPoint = "com.fortysevendegrees.main"
      }
    }
  }
  
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib"))
        implementation("io.arrow-kt:arrow-core:1.1.3")
        implementation("io.arrow-kt:arrow-fx-coroutines:1.1.3")
        implementation("io.arrow-kt:suspendapp:0.3.0")
        implementation("io.ktor:ktor-server-core:2.1.2")
        implementation("io.ktor:ktor-server-cio:2.1.2")
        implementation("io.ktor:ktor-client-core:2.1.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("app.softwork:postgres-native-sqldelight-driver:0.0.2")
      }
    }
  }
}