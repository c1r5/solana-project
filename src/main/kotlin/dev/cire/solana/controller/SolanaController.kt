package dev.cire.solana.controller

import dev.cire.solana.data.LogsNotification
import dev.cire.solana.data.ProgramNotification
import dev.cire.solana.service.SolanaService
import dev.cire.solana.service.SubscriptionMethods
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filterNotNull


fun Routing.solanaController() {
    val service = SolanaService.Builder()
        .subscription(SubscriptionMethods.LogsSubscribe(listOf("11111111111111111111111111111111")))
        .build()

    webSocket("/ws") {
        send(Frame.Text("Connected"))

        val parentJob = SupervisorJob()
        val wsScope = CoroutineScope(parentJob + Dispatchers.IO)

        wsScope.launch {
            service.incomingNotification
                .filterNotNull()
                .collect { notification ->

                }
        }

        wsScope.launch {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    if (message == "bye") {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                        parentJob.cancel() // Encerra todas as coroutines relacionadas
                    }
                }
            }
        }

        parentJob.join()
    }

    route("/solana") {
        get("/connect") {
            CoroutineScope(Job()).launch {
                service.connect()
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

