// package com.fortysevendegrees
//
// import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
// import com.fortysevendegrees.sqldelight.NativePostgres
// import io.kotest.assertions.assertSoftly
// import io.kotest.core.spec.style.StringSpec
// import io.kotest.matchers.shouldBe
// import io.ktor.client.request.get
// import io.ktor.client.statement.bodyAsText
// import io.ktor.http.HttpStatusCode
// import io.ktor.server.routing.routing
// import io.ktor.server.testing.testApplication
//
// class PingSpec : StringSpec({
//
//   "ping - pong".config(enabled = false) {
//     testApplication {
//       application { routing { ping() } }
//       val response = client.get("/ping")
//       response.status shouldBe HttpStatusCode.OK
//       response.bodyAsText() shouldBe "pong"
//     }
//   }
//
//   val email = "my-email@gmail.com"
//   val username = "my-username"
//   val pw = "non-encrypted"
//   val bio = "my-bio"
//   val image = "www.gravitar.com/my-username"
//
//   "Postgres construct" {
//     val driver = PostgresNativeDriver(
//       host = POSTGRES_HOST,
//       port = POSTGRES_PORT,
//       user = POSTGRES_USER,
//       database = POSTGRES_DB_NAME,
//       password = POSTGRES_PW
//     )
//     // NativePostgres(driver)
//   }
//
//   "Postgres create".config(enabled = false) {
//     val driver = PostgresNativeDriver(
//       host = POSTGRES_HOST,
//       port = POSTGRES_PORT,
//       user = POSTGRES_USER,
//       database = POSTGRES_DB_NAME,
//       password = POSTGRES_PW
//     )
//     try {
//       NativePostgres(driver)
//       NativePostgres.Schema.create(driver)
//     } finally {
//       driver.close()
//     }
//   }
//
//   "Postgres insert".config(enabled = false) {
//     val driver = PostgresNativeDriver(
//       host = POSTGRES_HOST,
//       port = POSTGRES_PORT,
//       user = POSTGRES_USER,
//       database = POSTGRES_DB_NAME,
//       password = POSTGRES_PW
//     )
//     try {
//       val postgres = NativePostgres(driver)
//       postgres.usersQueries.insert(
//         email = email,
//         username = username,
//         hashed_password = pw.encodeToByteArray(),
//         bio = bio,
//         image = image
//       )
//     } finally {
//       driver.close()
//     }
//   }
// })