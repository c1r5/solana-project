package dev.cire.solana.helper

fun extractAddress(content: String): String? {
    val data = content.substringAfter("Program").substringBefore("invoke").trim()
    return if (solanaAddressValidator(data)) data else null
}