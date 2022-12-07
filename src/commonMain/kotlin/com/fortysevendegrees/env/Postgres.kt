package com.fortysevendegrees.env

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import arrow.fx.coroutines.ResourceScope
import com.fortysevendegrees.sqldelight.NativePostgres

data class Postgres(
  val driver: PostgresNativeDriver,
  val database: NativePostgres
)

suspend fun ResourceScope.postgres(config: Env.Postgres): Postgres {
  val driver = install({
    PostgresNativeDriver(
      host = config.host,
      port = config.port,
      user = config.user,
      database = config.databaseName,
      password = config.password
    )
  }) { driver, _ -> driver.close() }
  driver.execute(null, "CREATE EXTENSION IF NOT EXISTS pgcrypto;", 0)
  val nativePostgres = NativePostgres(driver).also {
    NativePostgres.Schema.create(driver).await()
  }
  return Postgres(driver, nativePostgres)
}
