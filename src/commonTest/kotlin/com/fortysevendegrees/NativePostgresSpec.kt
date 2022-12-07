package com.fortysevendegrees

import app.cash.sqldelight.db.use
import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import arrow.fx.coroutines.resourceScope
import io.github.nomisrev.env.Env
import io.github.nomisrev.env.postgres
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NativePostgresSpec : StringSpec({

  beforeTest {
    val env = Env.Postgres()
    PostgresNativeDriver(
      host = env.host,
      port = env.port,
      user = env.user,
      database = env.databaseName,
      password = env.password
    ).use { it.execute(null, "DROP TABLE IF EXISTS users CASCADE;", parameters = 0) }
  }

  "insertUser" {
    resourceScope {
      val (driver, database) = postgres(Env.Postgres())
      val id = driver.insertAndGetId(
        "john.doe@gmail.com",
        "john.doe",
        "password",
        "I work at statefarm",
        "https://i.stack.imgur.com/xHWG8.jpg",
      )
      requireNotNull(id) { "id should not be null" }
      assertSoftly(database.usersQueries.selectById(id).executeAsOne()) {
        email shouldBe "john.doe@gmail.com"
        username shouldBe "john.doe"
        bio shouldBe "I work at statefarm"
        image shouldBe "https://i.stack.imgur.com/xHWG8.jpg"
      }
    }
  }

  "authenticate" {
    resourceScope {
      val (driver, database) = postgres(Env.Postgres())
      val id = driver.insertAndGetId(
        "john.doe1@gmail.com",
        "john.doe1",
        "password1",
        "I work at statefarm",
        "https://i.stack.imgur.com/xHWG8.jpg",
      )
      requireNotNull(id) { "id should not be null" }
      driver.authenticate("john.doe1", "password1") shouldBe "john.doe1"
    }
  }
})

suspend fun PostgresNativeDriver.insertAndGetId(
  email: String,
  username: String,
  password: String,
  bio: String,
  image: String
): Long? = executeQuery(
  1495379018,
  """
  INSERT INTO users (email, username, password, bio, image)
  VALUES ($1, $2, crypt($3, gen_salt('bf')), $4, $5)
  RETURNING id;
  """.trimIndent(),
  mapper = {
    it.next()
    it.getLong(0)
  },
  parameters = 5
) {
  bindString(0, email)
  bindString(1, username)
  bindString(2, password)
  bindString(3, bio)
  bindString(4, image)
}.await()

suspend fun PostgresNativeDriver.authenticate(
  username: String,
  password: String
): String? = executeQuery(
  null,
  """
  SELECT username
  FROM users
  WHERE username = $1 AND password = crypt($2, password);
  """.trimIndent(),
  mapper = {
    it.next()
    it.getString(0)
  },
  parameters = 2
) {
  bindString(0, username)
  bindString(1, password)
}.await()
