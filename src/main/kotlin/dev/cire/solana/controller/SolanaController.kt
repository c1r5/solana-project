package dev.cire.solana.controller

import dev.cire.solana.connection.SolanaRpc
import dev.cire.solana.connection.SolanaWebsocket
import dev.cire.solana.helper.transactionFactory
import dev.cire.solana.rpc.data.DefiPlatform
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun Routing.solanaRouting() {
    val parentJob = SupervisorJob();

    val ws = SolanaWebsocket()
    val rpc = SolanaRpc(Dispatchers.IO)

    val subscription = ws.logsSubscribe()

    route("/solana") {
        get("/connect") {
            subscription.launchIn(CoroutineScope(Dispatchers.Default + parentJob))
            call.respond(HttpStatusCode.OK)
        }
        get("/disconnect") {
            parentJob.cancel()
            call.respond(HttpStatusCode.OK)
        }
    }

    webSocket("/solana/ws") {
        send("Connected")

        val wsScope = CoroutineScope(parentJob + Dispatchers.IO)

        wsScope.launch {
            subscription
                .filterNotNull()
                .mapNotNull { response ->
                    transactionFactory(response)
                }.filter {transaction->
                    transaction.pool == DefiPlatform.PUMPFUN
                }.collect { transaction ->
                    send(Json.encodeToString(transaction))
                }
        }

        wsScope.launch {
            for (frame in incoming) {
                val text = frame as? Frame.Text ?: continue
                val content = text.readText()

                if (content == "bye") {
                    close(CloseReason(CloseReason.Codes.NORMAL, "closed by client"))
                }
            }
        }

        parentJob.join()
    }
}