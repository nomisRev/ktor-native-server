package io.github.nomisrev.env

import app.cash.sqldelight.ColumnAdapter
import app.softwork.sqldelight.postgresdriver.PostgresNativeDriver
import arrow.fx.coroutines.ResourceScope
import io.github.nomisrev.sqldelight.Articles
import io.github.nomisrev.sqldelight.Tags
import io.github.nomisrev.sqldelight.NativePostgres
import io.github.nomisrev.repo.ArticleId
import io.github.nomisrev.repo.UserId

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
  val nativePostgres = NativePostgres(
    driver,
    Articles.Adapter(articleIdAdapter, userIdAdapter),
    Tags.Adapter(articleIdAdapter)
  ).also {
    NativePostgres.Schema.create(driver).await()
  }
  return Postgres(driver, nativePostgres)
}

private val articleIdAdapter = columnAdapter(::ArticleId, ArticleId::serial)
private val userIdAdapter = columnAdapter(::UserId, UserId::serial)

private inline fun <A : Any, B> columnAdapter(
  crossinline decode: (databaseValue: B) -> A,
  crossinline encode: (value: A) -> B
): ColumnAdapter<A, B> =
  object : ColumnAdapter<A, B> {
    override fun decode(databaseValue: B): A = decode(databaseValue)
    override fun encode(value: A): B = encode(value)
  }
