package dev.cire.controller

import dev.cire.service.SolanaService
import dev.cire.service.SubscriptionMethods
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*


fun Routing.solanaController() {
    val service = SolanaService.Builder()
        .subscription(
            SubscriptionMethods.LogsSubscribe(
                listOf("11111111111111111111111111111111")
            )
        )
        .build()

    route("/solana") {
        get("/connect") {
            CoroutineScope(Job()).launch {
                service.connect().collect {
                    println(it)
                }
            }

            call.respond(
                message = HttpStatusCode.OK,
                typeInfo = null
            )
        }

        get("/close") {
            service.disconnect()
            call.respond(
                HttpStatusCode.OK,
                typeInfo = null
            )
        }
    }
}