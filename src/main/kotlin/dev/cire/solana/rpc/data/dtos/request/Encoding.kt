package dev.cire.solana.rpc.data.dtos.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Encoding {
    @SerialName("json")
    JSON,
    @SerialName("jsonParsed")
    JSON_PARSED,
    @SerialName("base64")
    BASE64,
    @SerialName("base58")
    BASE58
}