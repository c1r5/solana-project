package dev.cire.solana.connection

import dev.cire.solana.rpc.data.dtos.request.GetTransactionConfig
import dev.cire.solana.rpc.data.dtos.request.MethodRequest
import dev.cire.solana.rpc.data.dtos.response.GetTransactionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement


val client = HttpClient(CIO) {
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

data object Rpc {

    fun getTransaction(
        signature: String,
        config: GetTransactionConfig = GetTransactionConfig.default(),
    ) = callbackFlow {
        val params = mutableListOf(Json.encodeToJsonElement(signature))

        params.add(Json.encodeToJsonElement(config))

        val request = MethodRequest.from("getTransaction", params)

        val response = client.post(RpcUrl.MAINNNET.value) {
            setBody(request)
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) close(IllegalStateException("Unexpected response code ${response.status}"))
        if (response.bodyAsText().contains("error")) close(IllegalStateException("Unexpected error response"))

        val responseBody = response.body<GetTransactionResponse>()

        send(responseBody)

        awaitClose {}
    }


}







