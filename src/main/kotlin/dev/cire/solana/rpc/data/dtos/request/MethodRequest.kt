package dev.cire.solana.rpc.data.dtos.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MethodRequest (
    val jsonrpc: String,
    val id: Long,
    val method: String,
    val params: List<JsonElement>
) {
    companion object {
        fun from(method: String, params: List<JsonElement>): MethodRequest {
            return MethodRequest(
                jsonrpc = "2.0",
                id = 1,
                method = method,
                params = params
            )
        }
    }
}

@Serializable
data class GetTransactionConfig (
    val commitment: Commitment? = null,
    val maxSupportedTransactionVersion: Int? = null,
    val encoding : Encoding? = null,
) {
    companion object {
        fun default(): GetTransactionConfig {
            return GetTransactionConfig(
                encoding = Encoding.JSON,
                maxSupportedTransactionVersion = 0
            )
        }
    }
}

