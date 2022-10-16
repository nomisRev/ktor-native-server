package com.fortysevendegrees

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication

class PingSpec : StringSpec({
  
  "ping - pong" {
    testApplication {
      application { routing { ping() } }
      val response = client.get("/ping")
      response.status shouldBe HttpStatusCode.OK
      response.bodyAsText() shouldBe "pong"
    }
  }
  
  val email = "my-email@gmail.com"
  val username = "my-username"
  val pw = "non-encrypted"
  val bio = "my-bio"
  val image = "www.gravitar.com/my-username"
  
  "Postgres test".config(enabled = false) {
    postgres(Env.Postgres()).use { sqlDelight ->
      val userId = sqlDelight.usersQueries.insertAndGetId(
        "my-email@gmail.com",
        "my-username",
        "non-encrypted".encodeToByteArray(),
        "my-bio",
        "www.gravitar.com/my-username"
      ).executeAsOne()
      
      val selecyById = sqlDelight.usersQueries.selectById(userId).executeAsOne()
      
      assertSoftly {
        selecyById.email shouldBe email
        selecyById.username shouldBe username
        selecyById.hashed_password.decodeToString() shouldBe pw
        selecyById.bio shouldBe bio
        selecyById.image shouldBe image
      }
    }
  }
})