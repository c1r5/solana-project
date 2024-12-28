package dev.cire.solana.rpc.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class DefiPlatform (val address: String){
    PUMPFUN("6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P"),
//    RAYDIUMv2(""),
//    RAYDIUMv3(""),
    RAYDIUMv4("675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8")
}

@Serializable
sealed class TxInfo {
    @Serializable
    @SerialName("create_token")
    data class Create(
        @SerialName("token_address")
        val mint: String? = null,
        val symbol: String? = null,
        val name: String? = null,
        val uri: String? = null,
    ) : TxInfo()

    @Serializable
    @SerialName("unknown")
    data object Unknown: TxInfo()
}

@Serializable
data class Transaction(
    val signature: String? = null,
    val traderPublicKey: String? = null,
    val txInfo: TxInfo? = null,
    val initialBuy: Double? = null,
    val solAmount: Double? = null,
    val bondingCurveKey: String? = null,
    val vTokensInBondingCurve: Double? = null,
    val vSolInBondingCurve: Double? = null,
    val marketCapSol: Double? = null,
    val pool: DefiPlatform? = null,
)