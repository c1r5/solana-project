package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Instructions (

  @SerialName("accounts"       ) var accounts       : ArrayList<Int> = arrayListOf(),
  @SerialName("data"           ) var data           : String?           = null,
  @SerialName("programIdIndex" ) var programIdIndex : Int?              = null,
  @SerialName("stackHeight"    ) var stackHeight    : Int?           = null

)