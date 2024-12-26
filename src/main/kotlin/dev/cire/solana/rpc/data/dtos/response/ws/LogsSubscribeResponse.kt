package dev.cire.solana.rpc.data.dtos.response.ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class LogsSubscribeResponse (
  @SerialName("jsonrpc" ) var jsonrpc : String? = null,
  @SerialName("method"  ) var method  : String? = null,
  @SerialName("params"  ) var params  : Params? = Params()
)