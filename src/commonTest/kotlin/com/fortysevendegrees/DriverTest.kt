package com.fortysevendegrees

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import com.fortysevendegrees.sqldelight.NativePostgres
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
    val postgres = NativePostgres(driver)
    NativePostgres.Schema.create(driver)
  }
}