package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Meta (

    @SerialName("computeUnitsConsumed" ) var computeUnitsConsumed : Int?                         = null,
    @SerialName("err"                  ) var err                  : String?                      = null,
    @SerialName("fee"                  ) var fee                  : Long?                         = null,
    @SerialName("innerInstructions"    ) var innerInstructions    : ArrayList<InnerInstructions> = arrayListOf(),
    @SerialName("loadedAddresses"      ) var loadedAddresses      : LoadedAddresses?             = LoadedAddresses(),
    @SerialName("logMessages"          ) var logMessages          : ArrayList<String>            = arrayListOf(),
    @SerialName("postBalances"         ) var postBalances         : ArrayList<Long>               = arrayListOf(),
    @SerialName("postTokenBalances"    ) var postTokenBalances    : ArrayList<PostTokenBalances> = arrayListOf(),
    @SerialName("preBalances"          ) var preBalances          : ArrayList<Long>               = arrayListOf(),
    @SerialName("preTokenBalances"     ) var preTokenBalances     : ArrayList<PreTokenBalances>  = arrayListOf(),
    @SerialName("rewards"              ) var rewards              : ArrayList<String>            = arrayListOf(),
    @SerialName("status"               ) var status               : Status?                      = Status()

)