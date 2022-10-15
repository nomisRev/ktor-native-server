rootProject.name = "SuspendServer"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("libs.versions.toml"))
    }
  }
  
  repositories {
    mavenCentral()
  }
}
