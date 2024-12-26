package dev.cire.solana.rpc.data.dtos.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class InnerInstructions (

  @SerialName("index"        ) var index        : Int?                    = null,
  @SerialName("instructions" ) var instructions : ArrayList<Instructions> = arrayListOf()

)