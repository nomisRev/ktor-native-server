package com.fortysevendegrees

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.ResourceScope
import io.ktor.server.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngine.Configuration
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

suspend fun <Engine : ApplicationEngine, Config : Configuration> ResourceScope.server(
  factory: ApplicationEngineFactory<Engine, Config>,
  port: Int = 80,
  host: String = "0.0.0.0",
  configure: Config.() -> Unit = {},
  module: Application.() -> Unit,
): Engine =
  install({
    embeddedServer(
      factory,
      port = port,
      host = host,
      configure = configure,
      module = module
    ).also(ApplicationEngine::start)
  }) { engine, _ ->
    
    println("Waiting 30 seconds for Load Balancers & IP Tables to forgot us...")
    delay(30.seconds)
    println("Shutting down HTTP server...")
    engine.stop()
    println("HTTP server shutdown!")
  }
