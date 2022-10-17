package com.fortysevendegrees

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import kotlin.test.Test

class DriverTest {
  @Test
  fun constructDriver() {
    val driver = PostgresNativeDriver(
      host = "localhost",
      port = 5432,
      user = "postgres",
      database = "postgres",
      password = "postgres"
    )
  }
  
  @Test
  fun isRunning() {
    throw RuntimeException("This test is running")
  }
}