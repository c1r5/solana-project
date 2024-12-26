package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class GetTransactionResponse (

  @SerialName("jsonrpc" ) var jsonrpc : String? = null,
  @SerialName("result"  ) var result  : GetTransactionResult? = null,
  @SerialName("id"      ) var id      : Int?    = null

)