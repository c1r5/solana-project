package dev.cire.solana.connection

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Address {
    @SerialName("11111111111111111111111111111111")
    SYSTEM_ADDRESS
}