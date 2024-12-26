package dev.cire.solana.rpc.data.dtos.response.ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class LogsSubscribeResult (

  @SerialName("context" ) var context : Context? = Context(),
  @SerialName("value"   ) var value   : Value?   = Value()

)