package io.github.nomisrev

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.resourceScope
import io.github.nomisrev.env.Env
import io.github.nomisrev.env.postgres
import io.github.nomisrev.env.server
import io.github.nomisrev.routes.ping
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
