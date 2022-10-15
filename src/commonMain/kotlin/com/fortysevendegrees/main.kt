package com.fortysevendegrees

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.continuations.resource
import io.ktor.server.cio.CIO
import io.ktor.server.routing.routing
import kotlinx.coroutines.awaitCancellation

fun main() = SuspendApp {
  val env = Env()
  resource {
    @Suppress("UnusedPrivateMember")
    val database = postgres(env.postgres).bind()
    val engine = server(CIO,
      port = env.http.port,
      host = env.http.host
    ).bind()
    engine.application.routing {
      ping()
    }
  }.use { awaitCancellation() }
}
