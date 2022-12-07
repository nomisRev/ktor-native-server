package com.fortysevendegrees

import app.cash.sqldelight.db.use
import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import arrow.fx.coroutines.resourceScope
import com.fortysevendegrees.env.Env
import com.fortysevendegrees.env.postgres
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NativePostgresSpec : StringSpec({

  afterTest {
    val env = Env.Postgres()
    PostgresNativeDriver(
      host = env.host,
      port = env.port,
      user = env.user,
      database = env.databaseName,
      password = env.password
    ).use { it.execute(null, "DROP TABLE IF EXISTS users;", parameters = 0) }
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
