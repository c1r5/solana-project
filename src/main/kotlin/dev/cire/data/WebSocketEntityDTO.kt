package dev.cire.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class WebSocketEntityDTO(
    val jsonrpc: String,
    val id: Long,
    val method: WebsocketMethods,
    val params: List<JsonElement>
)

@Serializable
enum class WebsocketMethods {
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

class WebsocketRequestBuilder {
    private val jsonrpc = "2.0"
    private val id = 1L

    private var method: WebsocketMethods = WebsocketMethods.LOGS_SUBSCRIBE
    val params = mutableListOf<JsonElement>()

    fun method(method: WebsocketMethods) = apply { this.method = method }

    inline fun <reified T> param(param: T) = apply {
        this.params.add(Json.encodeToJsonElement(param))
    }

    fun build() = WebSocketEntityDTO(
        method = method,
        params = params,
        id = id,
        jsonrpc = jsonrpc,
    )
}