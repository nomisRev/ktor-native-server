package com.fortysevendegrees

import arrow.fx.coroutines.Resource
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngine.*
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

fun <TEngine : ApplicationEngine, TConfiguration : Configuration> server(
  factory: ApplicationEngineFactory<TEngine, TConfiguration>,
  port: Int = 80,
  host: String = "0.0.0.0"
): Resource<ApplicationEngine> =
  Resource(
    acquire = { embeddedServer(factory, port = port, host = host, module = {}).also(ApplicationEngine::start) },
    release = { engine, _ ->
      println("Waiting 30 seconds for Load Balancers & IP Tables to forgot us...")
      delay(30.seconds)
      println("Shutting down HTTP server...")
      engine.stop()
      println("HTTP server shutdown!")
    })
