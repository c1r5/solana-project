package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class PostTokenBalances (

  @SerialName("accountIndex"  ) var accountIndex  : Int?           = null,
  @SerialName("mint"          ) var mint          : String?        = null,
  @SerialName("owner"         ) var owner         : String?        = null,
  @SerialName("programId"     ) var programId     : String?        = null,
  @SerialName("uiTokenAmount" ) var uiTokenAmount : UiTokenAmount? = UiTokenAmount()

)