package dev.cire.solana.service

import dev.cire.helpers.SOLANA_RPC
import dev.cire.solana.connection.Rpc
import dev.cire.solana.data.LogsNotification
import dev.cire.solana.data.ProgramNotification
import dev.cire.solana.rpc.data.dtos.request.Commitment
import dev.cire.solana.data.SolanaNotification
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement


private val client = HttpClient(CIO) {
    install(WebSockets)
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}

class SolanaService (
    val subscription: SubscriptionMethods
) {
    private val _isClosed = MutableStateFlow(true)
    private val _incomingNotification = MutableStateFlow<SolanaNotification?>(null)

    val incomingNotification: StateFlow<SolanaNotification?> = _incomingNotification.asStateFlow()

    data class Builder(
        private var subscribe: SubscriptionMethods? = null,
    ) {
        fun subscription(subscription: SubscriptionMethods) = apply { this.subscribe = subscription }

        fun build() = SolanaService(
            subscription = subscribe ?: SubscriptionMethods.LogsSubscribe()
        )
    }

    suspend fun connect() {
        client.webSocket(method = HttpMethod.Get, host = Url(SOLANA_RPC).host) {
            val encodedRequest = Json.encodeToString(
                MethodRequestBody.builder {
                    method = subscription.name
                    params = subscription.params
                }
            )

            send(encodedRequest)

            println("Sent data: $encodedRequest")

            _isClosed.update { false }

            do {
                val content = incoming.receive() as? Frame.Text ?: continue

                val text = content.readText()
                if (text.contains("\"params\"")) {
                    val notification= Json.decodeFromString<SolanaNotification>(text)
                    when(val method = notification.params?.notificationResult?.value) {
                        is LogsNotification -> {
                            val transaction = method.signature?.let { Rpc.getTransaction(it).first() }

                            if (transaction == null) continue

                            println(transaction)
                        }

                        is ProgramNotification -> {

                        }
                    }
                }

            } while (!_isClosed.value)

            close()

            _isClosed.update { true }
        }
    }

    fun disconnect() {
        _isClosed.update { true }
    }
}

@Serializable
internal data class MethodRequestBody (
    private val jsonrpc: String?,
    private val id: Int?,
    private val method: SubscriptionMethodName?,
    private val params: List<JsonElement>?
) {
    companion object {
        fun builder(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    data class Builder(
        var jsonrpc: String? = "2.0",
        var id: Int? = 1,
        var method: SubscriptionMethodName? = null,
        var params: List<JsonElement>? = null,
    ) {
        fun build() = MethodRequestBody(
            jsonrpc,
            id,
            method,
            params
        )
    }
}

@Serializable
enum class SubscriptionMethodName {
    @SerialName("accountSubscribe")
    ACCOUNT_SUBSCRIBE,
    @SerialName("accountUnsubscribe")
    ACCOUNT_UNSUBSCRIBE,
    @SerialName("blockSubscribe")
    BLOCK_SUBSCRIBE,
    @SerialName("blockUnsubscribe")
    BLOCK_UNSUBSCRIBE,
    @SerialName("logsSubscribe")
    LOGS_SUBSCRIBE,
    @SerialName("logsUnsubscribe")
    LOGS_UNSUBSCRIBE,
    @SerialName("programSubscribe")
    PROGRAM_SUBSCRIBE,
    @SerialName("programUnsubscribe")
    PROGRAM_UNSUBSCRIBE,
    @SerialName("rootSubscribe")
    ROOT_SUBSCRIBE,
    @SerialName("rootUnsubscribe")
    ROOT_UNSUBSCRIBE,
    @SerialName("signatureSubscribe")
    SIGNATURE_SUBSCRIBE,
    @SerialName("signatureUnsubscribe")
    SIGNATURE_UNSUBSCRIBE,
    @SerialName("slotSubscribe")
    SLOT_SUBSCRIBE,
    @SerialName("slotUnsubscribe")
    SLOT_UNSUBSCRIBE,
    @SerialName("slotsUpdatesSubscribe")
    SLOTS_UPDATES_SUBSCRIBE,
    @SerialName("slotsUpdatesUnsubscribe")
    SLOTS_UPDATES_UNSUBSCRIBE,
    @SerialName("voteSubscribe")
    VOTE_SUBSCRIBE,
    @SerialName("voteUnsubscribe")
    VOTE_UNSUBSCRIBE
}

sealed class SubscriptionMethods(
    var params: List<JsonElement> = emptyList(),
    var name: SubscriptionMethodName? = null,
) {
    data class LogsSubscribe(
        val mentions: List<String>? = null,
        val commitment: Commitment? = null,

        ): SubscriptionMethods() {
        init {
            params = mutableListOf<JsonElement>().apply {
                    if (mentions == null && commitment == null) {
                        return@apply add(0, Json.encodeToJsonElement("all"))
                    }

                    mentions?.let {
                        add(Json.encodeToJsonElement(mapOf("mentions" to mentions)))
                    }

                    commitment?.let {
                        add(Json.encodeToJsonElement(mapOf("commitment" to commitment)))
                    }
                }
            name = SubscriptionMethodName.LOGS_SUBSCRIBE
        }
    }
}

