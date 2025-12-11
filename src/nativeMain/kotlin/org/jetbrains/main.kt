package org.jetbrains

import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import platform.posix.getenv

private const val PORT: Int = 8080
private const val LOCAL_HOST: String = "0.0.0.0"

data class Env(
    val http: Http = Http(),
) {
    data class Http @OptIn(ExperimentalForeignApi::class) constructor(
        val host: String = getenv("HOST")?.toKString() ?: LOCAL_HOST,
        val port: Int = getenv("SERVER_PORT")?.toKString()?.toIntOrNull() ?: PORT,
    )
}

fun main() {
    val env = Env()
    @Suppress("UnusedPrivateMember")
    embeddedServer(
        CIO,
        port = env.http.port,
        host = env.http.host
    ) {
        ping()
    }.start(wait = true)
}

fun Application.ping() = routing {
    get("/ping") {
        call.respondText("Pong")
    }
}
