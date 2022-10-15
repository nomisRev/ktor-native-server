package com.fortysevendegrees

import kotlinx.cinterop.toKString
import platform.posix.getenv
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

private const val PORT: Int = 8080
private const val POSTGRES_HOST: String = "localhost"
private const val POSTGRES_PORT: Int = 5432
private const val POSTGRES_USER: String = "postgres"
private const val POSTGRES_PW: String = "postgres"
private const val POSTGRES_DB_NAME: String = "postgres"
private const val AUTH_SECRET: String = "MySuperStrongSecret"
private const val AUTH_ISSUER: String = "KtorArrowExampleIssuer"
private const val AUTH_DURATION: Int = 30

data class Env(
  val postgres: Postgres = Postgres(),
  val http: Http = Http(),
  val auth: Auth = Auth(),
) {
  data class Http(
    val host: String = getenv("HOST")?.toKString() ?: "0.0.0.0",
    val port: Int = getenv("SERVER_PORT")?.toKString()?.toIntOrNull() ?: PORT,
  )
  
  data class Postgres(
    val host: String = getenv("POSTGRES_HOST")?.toKString() ?: POSTGRES_HOST,
    val port: Int = getenv("POSTGRES_PORT")?.toKString()?.toIntOrNull() ?: POSTGRES_PORT,
    val user: String = getenv("POSTGRES_USERNAME")?.toKString() ?: POSTGRES_USER,
    val databaseName: String = getenv("POSTGRES_DB_NAME")?.toKString() ?: POSTGRES_DB_NAME,
    val password: String = getenv("POSTGRES_PASSWORD")?.toKString() ?: POSTGRES_PW,
  )
  
  data class Auth(
    val secret: String = getenv("JWT_SECRET")?.toKString() ?: AUTH_SECRET,
    val issuer: String = getenv("JWT_ISSUER")?.toKString() ?: AUTH_ISSUER,
    val duration: Duration = (getenv("JWT_DURATION")?.toKString()?.toIntOrNull() ?: AUTH_DURATION).days
  )
}