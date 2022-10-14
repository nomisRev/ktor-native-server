package com.fortysevendegrees

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.continuations.resource
import io.ktor.server.cio.CIO
import io.ktor.server.routing.routing
import kotlinx.coroutines.awaitCancellation

val config = PostgresConfig("localhost", 5432, "postgres", "postgres", "password")

fun main() = SuspendApp {
  resource {
    val database = postgres(config).bind()
    val engine = server(CIO, port = 8080).bind()
    engine.application.routing {
      ping()
    }
  }.use { awaitCancellation() }
}
