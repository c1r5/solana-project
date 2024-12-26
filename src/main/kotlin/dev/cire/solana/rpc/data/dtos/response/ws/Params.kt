package dev.cire.solana.rpc.data.dtos.response.ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Params (

  @SerialName("result"       ) var result       : LogsSubscribeResult? = LogsSubscribeResult(),
  @SerialName("subscription" ) var subscription : Long?    = null

)