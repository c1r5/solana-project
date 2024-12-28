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
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun Routing.solanaRouting() {
    val parentJob = SupervisorJob()

    val websocketClientScope = CoroutineScope(Dispatchers.Default + parentJob)
    val websocketServerScope = CoroutineScope(Dispatchers.IO + parentJob);

    val rpc = SolanaRpc(Dispatchers.IO)
    val ws = SolanaWebsocket()

    val subscription = ws.logsSubscribe()
        .map { Transaction.from(it) }
        .filter { it.pool == DefiPlatform.PUMPFUN && it.txInfo is TxInfo.Create}
        .mapNotNull { transaction ->
            val details = rpc.getTransaction(transaction.signature).getOrNull() ?: return@mapNotNull null

            transaction.apply {
                txInfo = when (transaction.txInfo) {
                    is TxInfo.Create -> (transaction.txInfo as TxInfo.Create)
                        .apply {
                            details.getInfo("create")?.let { mint = it.mint }
                        }

                    else -> transaction.txInfo
                }
            }
        }
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
            subscription.collect { transaction ->
                send(Json.encodeToString(transaction))
            }
        }

        websocketServerScope.launch {
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