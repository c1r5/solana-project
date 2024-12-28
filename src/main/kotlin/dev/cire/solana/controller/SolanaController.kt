package dev.cire.solana.controller

import dev.cire.solana.connection.SolanaRpc
import dev.cire.solana.connection.SolanaWebsocket
import dev.cire.solana.rpc.data.DefiPlatform
import dev.cire.solana.rpc.data.Transaction
import dev.cire.solana.rpc.data.TxInfo
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun Routing.solanaRouting() {
    val websocketServerScope = CoroutineScope(Dispatchers.IO + SupervisorJob());
    val websocketClientScope = CoroutineScope(Dispatchers.Default + SupervisorJob());

    val ws = SolanaWebsocket()
    val rpc = SolanaRpc(Dispatchers.IO)

    val subscription = ws.logsSubscribe()

    route("/solana") {
        get("/connect") {
            subscription.launchIn(websocketClientScope)
            call.respond(HttpStatusCode.OK)
        }
        get("/disconnect") {
            websocketClientScope.cancel()
            call.respond(HttpStatusCode.OK)
        }
    }

    webSocket("/solana/ws") {
        send("Connected")

        websocketServerScope.launch {
            subscription
                .filterNotNull()
                .mapNotNull { response ->
                    Transaction.from(response)
                }
                .filter { transaction ->
                    transaction.pool == DefiPlatform.PUMPFUN && transaction.txInfo is TxInfo.Create
                }
                .mapNotNull {transaction ->
                    transaction.apply {
                        val transactionResponse  = rpc.getTransaction(signature).getOrNull()
                        txInfo = when (txInfo) {
                            is TxInfo.Create -> transactionResponse
                                ?.getInfo("create")
                                ?.let { TxInfo.Create(mint = it.mint) }
                            else -> txInfo
                        }
                        transactionResponse?.getInfo("transfer")?.source?.let { traderPublicKey = it }
                    }
                }
                .collect { transaction ->
                    send(Json.encodeToString(transaction))
                }
        }

        websocketServerScope.launch {
            for (frame in incoming) {
                val text = frame as? Frame.Text ?: continue
                val content = text.readText()

                if (content == "bye") {
                    close(CloseReason(CloseReason.Codes.NORMAL, "closed by client"))
                    websocketServerScope.cancel()
                }
            }
        }

        websocketServerScope.coroutineContext.job.join()
    }
}