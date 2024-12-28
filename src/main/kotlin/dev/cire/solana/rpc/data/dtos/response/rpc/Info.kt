package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val account: String? = null,
    val mint: String? = null,
    val decimals: Int? = null,
    val mintAuthority: String? = null,
    val source: String? = null,
    val systemProgram: String? = null,
    val tokenProgram: String? = null,
    val wallet: String? = null,
    val lamports: Long? = null,
    val newAccount: String? = null,
    val owner: String? = null,
    val space: Int? = null,
    val extensionTypes: List<String>? = null,
    val destination: String? = null,
    val amount: Long? = null,
    val authority: String? = null,
    val authorityType: String? = null,
    val newAuthority: String? = null,
)
