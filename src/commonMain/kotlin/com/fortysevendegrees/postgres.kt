package com.fortysevendegrees

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import arrow.fx.coroutines.Resource
import com.fortysevendegrees.sqldelight.NativePostgres

data class PostgresConfig(
  val host: String = "localhost",
  val port: Int = 5432,
  val user: String = "postgres",
  val databaseName: String = "postgres",
  val password: String = "password",
)

fun postgres(config: PostgresConfig): Resource<NativePostgres> =
  Resource({
    PostgresNativeDriver(
      host = config.host,
      port = config.port,
      user = config.user,
      database = config.databaseName,
      password = config.password
    )
  }) { driver, _ -> driver.close() }
    .map { NativePostgres(it) }
