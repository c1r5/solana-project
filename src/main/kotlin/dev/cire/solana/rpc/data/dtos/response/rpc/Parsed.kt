package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parsed (
    @SerialName("info"  ) var info: Info? = null,
    @SerialName("type"  ) var type: String? = null,
)
