package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable

@Serializable
data class AccountKey (
    val pubkey: String? = null,
    val signer: Boolean? = null,
    val source: String? = null,
    val writable: Boolean? = null,
)