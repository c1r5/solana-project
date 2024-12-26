package dev.cire.solana.rpc.data.dtos.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Commitment {
    @SerialName("finalized")
    FINALIZED,
    @SerialName("processed")
    PROCESSED,
    @SerialName("confirmed")
    CONFIRMED,
}