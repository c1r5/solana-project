package dev.cire.solana.helper

import dev.cire.solana.rpc.data.TxInfo

typealias TxTypeDetector = (Logs) -> TxInfo?
typealias TxTypeDetectorRule = (ProgramInstructions) -> Boolean

val pumpfunIsCreateCalled: TxTypeDetectorRule = {content ->
    content.any { it.second == "Create" }
}

val isTokenCreationProgramCalled: TxTypeDetectorRule = {content ->
    content.any { it.first.startsWith("Token") && it.second == "InitializeMint2" }
}

val pumpfunTxInfoDetector: TxTypeDetector = { logs ->
    val programInstructions = extractInstructions(logs)

    val isCreateCalled = pumpfunIsCreateCalled(programInstructions)
    val isTokenCreationProgramCalled = isTokenCreationProgramCalled(programInstructions)

    when {
        isCreateCalled && isTokenCreationProgramCalled -> TxInfo.Create()
        else -> null
    }
}

