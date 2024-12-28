package dev.cire.solana.rpc.data

import dev.cire.solana.helper.*
import dev.cire.solana.rpc.data.dtos.response.ws.LogsSubscribeResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class DefiPlatform (var address: String){
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
        var mint: String? = null,
        var symbol: String? = null,
        var name: String? = null,
        var uri: String? = null,
    ) : TxInfo()

    @Serializable
    @SerialName("unknown")
    data object Unknown: TxInfo()
}

@Serializable
data class Transaction(
    var signature: String? = null,
    var slot: Long? = null,
    var traderPublicKey: String? = null,
    var txInfo: TxInfo? = null,
    var initialBuy: Double? = null,
    var solAmount: Double? = null,
    var bondingCurveKey: String? = null,
    var vTokensInBondingCurve: Double? = null,
    var vSolInBondingCurve: Double? = null,
    var marketCapSol: Double? = null,
    var pool: DefiPlatform? = null,
) {
    companion object {
        fun from (response: LogsSubscribeResponse): Transaction {
            val logs: Logs = response.params?.result?.value?.logs

            return Transaction(
                signature = response.params?.result?.value?.signature,
                slot = response.params?.result?.context?.slot,
                traderPublicKey = null,
                txInfo = logs.txInfo() ?: TxInfo.Unknown,
                solAmount = null,
                marketCapSol = null,
                pool = logs.pool()
            )
        }
    }
}