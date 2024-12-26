package dev.cire.solana.controller

import dev.cire.solana.connection.SolanaWebsocket
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn


fun Routing.solanaRouting() {
    val parentJob = SupervisorJob();

    val ws = SolanaWebsocket()
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
            subscription.filterNotNull().collect {response ->
                response.params?.result?.value?.signature?.let { send(it) }
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