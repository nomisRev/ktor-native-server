package com.fortysevendegrees

import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import arrow.fx.coroutines.Resource
import com.fortysevendegrees.sqldelight.NativePostgres

fun postgres(config: Env.Postgres): Resource<NativePostgres> =
  Resource({
    PostgresNativeDriver(
      host = config.host,
      port = config.port,
      user = config.user,
      database = config.databaseName,
      password = config.password
    )
  }) { driver, _ -> driver.close() }
    .map { driver ->
      NativePostgres(driver)
        .also { NativePostgres.Schema.migrate(driver, 0, NativePostgres.Schema.version) }
    }
