package dev.cire.solana.rpc.data.dtos.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class UiTokenAmount (

  @SerialName("amount"         ) var amount         : String? = null,
  @SerialName("decimals"       ) var decimals       : Int?    = null,
  @SerialName("uiAmount"       ) var uiAmount       : Double? = null,
  @SerialName("uiAmountString" ) var uiAmountString : String? = null

)