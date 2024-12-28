package dev.cire.solana.rpc.data.dtos.response.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class GetTransactionResponse (
  var jsonrpc : String? = null,
  var result  : GetTransactionResult? = null,
  var id      : Int?    = null
) {
  fun getInfo(instructionType: String) = result
    ?.meta
    ?.innerInstructions
    ?.flatMap { it.instructions }
    ?.find { instruction -> instruction.parsed?.type == instructionType }
    ?.parsed
    ?.info
}