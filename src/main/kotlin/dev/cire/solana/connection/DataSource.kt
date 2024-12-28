package dev.cire.solana.connection

import dev.cire.solana.rpc.data.dtos.request.GetTransaction
import dev.cire.solana.rpc.data.dtos.request.SubscribeMethod
import dev.cire.solana.rpc.data.dtos.response.rpc.GetTransactionResponse
import dev.cire.solana.rpc.data.dtos.response.ws.LogsSubscribeResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


private val client = HttpClient(CIO) {
    install(WebSockets)
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
        })
    }
}

class SolanaRpc (private val dispatcher: CoroutineDispatcher) {
    suspend fun getTransaction(
        signature: String?,
        config: GetTransaction = GetTransaction.default(),
    ) = runCatching {
        withContext(dispatcher) {
            if (signature == null) error("Signature isn't supported")

            val params = mutableListOf(Json.encodeToJsonElement(signature))

            params.add(Json.encodeToJsonElement(config))

            val request = SubscribeMethod.from("getTransaction", params)

            val response = client.post(RpcUrl.MAINNNET.value) {
                setBody(request)
                contentType(ContentType.Application.Json)
            }

            if (!response.status.isSuccess()) error(IllegalStateException("Unexpected response code ${response.status}"))
            if (response.bodyAsText().contains("error")) error(IllegalStateException("Unexpected error response"))

            response.body<GetTransactionResponse>()
        }
    }
}

class SolanaWebsocket {
    private val websocketURL = Url(RpcUrl.WSNODE.value)

    fun logsSubscribe() = flow {
        client.webSocket(
            method = HttpMethod.Get,
            host = websocketURL.host
        ) {

            val methodRequest = SubscribeMethod.from(method = "logsSubscribe")

            val encoded = Json.encodeToString(methodRequest)

            send(encoded)

            while (true) {
                for (frame in incoming) {
                    val text = frame as? Frame.Text ?: continue
                    val content = text.readText()
                    if (!content.contains("\"logsNotification\"")) continue

                    val decoded = Json.decodeFromString<LogsSubscribeResponse>(content)

                    emit(decoded)
                }
            }
        }
    }
}




