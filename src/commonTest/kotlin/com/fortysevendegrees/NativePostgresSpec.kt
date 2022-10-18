package com.fortysevendegrees

import app.cash.sqldelight.db.use
import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private const val email = "my-email@gmail.com"
private const val username = "my-username"
private const val bio = "my-bio"
private const val image = "www.gravitar.com/my-username"

class NativePostgresSpec : StringSpec({
  
  afterTest {
    PostgresNativeDriver(
      host = POSTGRES_HOST,
      port = POSTGRES_PORT,
      user = POSTGRES_USER,
      database = POSTGRES_DB_NAME,
      password = POSTGRES_PW
    ).use { it.execute(null, "DROP TABLE IF EXISTS users;", parameters = 0) }
  }
  
  "Postgres insert" {
    postgres(Env.Postgres()).use { db ->
      val userId = db.usersQueries.insertAndGetId(
        email = email,
        username = username,
        bio = bio,
        image = image
      ).executeAsOne()
      val user = db.usersQueries.selectById(userId).executeAsOne()
      assertSoftly {
        user.email shouldBe email
        user.username shouldBe username
        user.image shouldBe image
        user.bio shouldBe bio
      }
    }
  }
})