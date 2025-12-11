package org.jetbrains

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class PingSpec {
    @Test
    fun `ping route`() {
        testApplication {
            application { ping() }
            val response = client.get("/ping")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("Pong", response.bodyAsText())
        }
    }
}
