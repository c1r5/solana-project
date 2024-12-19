package dev.cire.controller

import dev.cire.data.WebsocketMethods
import dev.cire.data.WebsocketRequestBuilder
import dev.cire.helpers.PUMPFUN_PROGRAM_ADDRESS
import dev.cire.service.SolanaService
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


fun Routing.solanaController() {
    val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val service = SolanaService()

    route("/solana") {
        get("/connect") {
            CoroutineScope(Job()).launch {
                val webSocketRequest = WebsocketRequestBuilder()
                    .method(WebsocketMethods.PROGRAM_SUBSCRIBE)
                    .param(PUMPFUN_PROGRAM_ADDRESS)
                    .param(mapOf("encoding" to "jsonParsed"))
                    .build()

                service.connect(webSocketRequest).onEach {
                    println(it)
                }.launchIn(coroutineScope)
            }
        }

        get("/close") {
            service.closeWebsocket()
            call.respond(
                HttpStatusCode.OK,
                typeInfo = null
            )
        }
    }
}