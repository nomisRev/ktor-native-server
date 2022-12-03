package com.fortysevendegrees

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.resourceScope
import com.fortysevendegrees.env.Env
import com.fortysevendegrees.env.postgres
import com.fortysevendegrees.env.server
import com.fortysevendegrees.routes.ping
import io.ktor.server.cio.CIO
import io.ktor.server.routing.routing
import kotlinx.coroutines.awaitCancellation

fun main() = SuspendApp {
  val env = Env()
  resourceScope {
    @Suppress("UnusedPrivateMember")
    val database = postgres(env.postgres)
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
