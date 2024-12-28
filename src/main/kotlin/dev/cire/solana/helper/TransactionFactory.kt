package dev.cire.solana.helper

import dev.cire.solana.rpc.data.DefiPlatform
import dev.cire.solana.rpc.data.Transaction
import dev.cire.solana.rpc.data.TxInfo
import dev.cire.solana.rpc.data.dtos.response.ws.LogsSubscribeResponse

typealias Logs = List<String>

fun transactionFactory(response: LogsSubscribeResponse): Transaction? {
    val logs: Logs = response.params?.result?.value?.logs ?: return null
    val signature = response.params?.result?.value?.signature;
    val pool = getPool(logs) ?: return null

    val txInfo = getTxInfo(logs)

    return Transaction(
        signature = signature,
        traderPublicKey = null,
        txInfo = txInfo ?: TxInfo.Unknown,
        initialBuy = null,
        solAmount = null,
        bondingCurveKey = null,
        vTokensInBondingCurve = null,
        vSolInBondingCurve = null,
        marketCapSol = null,
        pool = pool
    )
}




fun getTxInfo(logs: Logs?): TxInfo? {
    if (logs.isNullOrEmpty()) return null

    return pumpfunTxInfoDetector(logs)
}

fun getPool (logs: Logs?): DefiPlatform? {
    if (logs.isNullOrEmpty()) return null

    return when {
        pumpfunDetector(logs) -> DefiPlatform.PUMPFUN
        raydiumDetector(logs) -> DefiPlatform.RAYDIUMv4
        else -> null
    }
}
