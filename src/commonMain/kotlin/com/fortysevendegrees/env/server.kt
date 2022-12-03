package com.fortysevendegrees.env

import arrow.fx.coroutines.ResourceScope
import io.ktor.server.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngine.Configuration
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

suspend fun <Engine : ApplicationEngine, Config : Configuration> ResourceScope.server(
  factory: ApplicationEngineFactory<Engine, Config>,
  port: Int = 80,
  host: String = "0.0.0.0",
  preWait: Duration = 30.seconds,
  grace: Duration = 1.seconds,
  timeout: Duration = 5.seconds,
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
    if (!engine.environment.developmentMode) {
      engine.environment.log.info(
        "Waiting for $preWait for Load Balancers & IP Tables to forgot us..., turn it off using io.ktor.development=true"
      )
      delay(preWait)
    }
    println("Shutting down HTTP server...")
    engine.stop(grace.inWholeMilliseconds, timeout.inWholeMilliseconds)
    println("HTTP server shutdown!")
  }
