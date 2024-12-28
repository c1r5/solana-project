package dev.cire.solana.helper


fun extractInstructions(logs: Logs): ProgramInstructions {
    return logs?.mapIndexedNotNull { index, s ->
        val instruction = logs.getOrNull(index + 1)
            ?.substringAfterLast("Program log: Instruction:")
            ?.trim()
            ?: return@mapIndexedNotNull null

        val address = extractAddress(s)
            ?: return@mapIndexedNotNull null

        address to instruction
    } ?: emptyList()
}