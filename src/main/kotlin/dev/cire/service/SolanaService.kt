package dev.cire.service

import dev.cire.data.WebSocketEntityDTO
import dev.cire.data.WsNotification
import dev.cire.helpers.SOLANA_RPC
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.sol4k.Connection
import org.sol4k.RpcUrl
import org.sol4k.api.Commitment


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

class SolanaService {
    private val connection = Connection(RpcUrl.DEVNET, commitment = Commitment.CONFIRMED)
    private val solanaWebsocketURL = Url(SOLANA_RPC)
    private val _readableContent = MutableStateFlow<Frame.Text?>(null)
    val readableContent: StateFlow<Frame.Text?> = _readableContent.asStateFlow()

    private val _isClosed = MutableStateFlow(true)

    fun closeWebsocket() = _isClosed.update { true }

    fun connect(request: WebSocketEntityDTO) = callbackFlow {
        client.webSocket(method = HttpMethod.Get, host = solanaWebsocketURL.host) {
            val encodedRequest = Json.encodeToString(request)
            send(encodedRequest)
            _isClosed.update { false }
            try {
                while (!_isClosed.value) {
                    val content = incoming.receive() as? Frame.Text
                    content?.let {
                        val text = content.readText()
                        if (text.contains("\"params\"")) {
                            val deserialized = Json.decodeFromString<WsNotification>(text)
                            trySend(deserialized)
                        }
                    }
                }
            } catch (e: Throwable) {
                this@callbackFlow.close(e)
            } finally {
                close()
            }
        }

        awaitClose {
            _isClosed.update { true }
        }
    }
}



