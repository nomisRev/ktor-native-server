package com.fortysevendegrees

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.resourceScope
import io.ktor.server.cio.CIO
import io.ktor.server.routing.routing
import kotlinx.coroutines.awaitCancellation

fun main() = SuspendApp {
  val env = Env()
  resourceScope {
    @Suppress("UnusedPrivateMember")
    val database = postgres(env.postgres).bind()
    server(
      CIO,
      port = env.http.port,
      host = env.http.host
    ) {
      routing {
        ping()
      }
    }
    awaitCancellation()
  }
}
