package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class GetTransactionResult(
    var blockTime: Int? = null,
    var meta: Meta? = Meta(),
    var slot: Int? = null,
    var transaction: Transaction? = Transaction(),
    var version: Int? = null
)