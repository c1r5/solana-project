package dev.cire.solana.helper

import dev.cire.solana.rpc.data.DefiPlatform
import dev.cire.solana.rpc.data.TxInfo

fun Logs.txInfo(): TxInfo? {
    if (isNullOrEmpty()) return null
    return pumpfunTxInfoDetector(this)
}

fun Logs.pool(): DefiPlatform? {
    if (isNullOrEmpty()) return null
    return when {
        pumpfunDetector(this) -> DefiPlatform.PUMPFUN
        raydiumDetector(this) -> DefiPlatform.RAYDIUMv4
        else -> null
    }
}


