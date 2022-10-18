package com.fortysevendegrees

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import co.touchlab.stately.concurrency.ThreadRef
import com.fortysevendegrees.sqldelight.NativePostgres
import io.ktor.util.ThreadInfo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DriverTest {
  
  val email = "my-email2@gmail.com"
  val username = "my-username2"
  val pw = "non-encrypted"
  val bio = "my-bio"
  val image = "www.gravitar.com/my-username"
  
  @Test
  fun constructDriver() = runBlocking {
    val driver = PostgresNativeDriver(
      host = "localhost",
      port = 5432,
      user = "postgres",
      database = "postgres",
      password = "postgres"
    )
    driver.execute(null, "DROP TABLE IF EXISTS users;", parameters = 0)
    val postgres = NativePostgres(driver)
    NativePostgres.Schema.create(driver).await()
    val userId = postgres.usersQueries.insertAndGetId(
      email = email,
      username = username,
      bio = bio,
      image = image
    ).executeAsOne()
    val user = postgres.usersQueries.selectById(userId).executeAsOne()
    assertEquals(user.email, email)
    assertEquals(user.username, username)
    assertEquals(user.bio, bio)
    assertEquals(user.image, image)
  }
}