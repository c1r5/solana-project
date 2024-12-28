package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class GetTransactionResponse (
  var jsonrpc : String? = null,
  var result  : GetTransactionResult? = null,
  var id      : Int?    = null
)