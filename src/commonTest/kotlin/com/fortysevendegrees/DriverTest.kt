package com.fortysevendegrees

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import com.fortysevendegrees.sqldelight.NativePostgres
import kotlin.test.Test

class DriverTest {
  
  val email = "my-email@gmail.com"
  val username = "my-username"
  val pw = "non-encrypted"
  val bio = "my-bio"
  val image = "www.gravitar.com/my-username"
  
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
    postgres.usersQueries.insert(
      email = email,
      username = username,
      bio = bio,
      image = image
    )
  }
}