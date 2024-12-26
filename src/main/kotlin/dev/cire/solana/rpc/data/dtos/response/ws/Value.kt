package dev.cire.solana.rpc.data.dtos.response.ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Value (

  @SerialName("err"       ) var err       : Err?           = null,
  @SerialName("logs"      ) var logs      : ArrayList<String> = arrayListOf(),
  @SerialName("signature" ) var signature : String?           = null

)