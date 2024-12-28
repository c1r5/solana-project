package dev.cire.solana.rpc.data.dtos.request

import kotlinx.serialization.Serializable

@Serializable
data class GetTransaction (
    val commitment: Commitment? = null,
    val maxSupportedTransactionVersion: Int? = null,
    val encoding : Encoding? = null,
) {
    companion object {
        fun default(): GetTransaction {
            return GetTransaction(
                encoding = Encoding.JSON_PARSED,
                maxSupportedTransactionVersion = 0
            )
        }
    }
}