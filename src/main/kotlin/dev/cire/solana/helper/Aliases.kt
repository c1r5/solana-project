package dev.cire.solana.helper

typealias Logs = List<String>?
typealias AddressValidator = (String) -> Boolean
typealias ProgramAddress = String
typealias Instruction = String
typealias ProgramInstructions = List<Pair<ProgramAddress, Instruction>>
typealias PoolDetector = (List<String>) -> Boolean
