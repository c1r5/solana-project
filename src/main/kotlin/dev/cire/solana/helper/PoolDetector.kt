package dev.cire.solana.helper

import dev.cire.solana.rpc.data.DefiPlatform


val pumpfunDetector: PoolDetector = { data ->
    data.any { DefiPlatform.PUMPFUN.address in it }
}

val raydiumDetector: PoolDetector = { data ->
    data.any { "ray_log" in it } && data.any { DefiPlatform.RAYDIUMv4.address in it }
}