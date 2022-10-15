package com.fortysevendegrees

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.ping(): Route =
  get("/ping") { call.respond("pong") }
