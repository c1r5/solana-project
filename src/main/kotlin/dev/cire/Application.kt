package dev.cire

import dev.cire.server.configureRouting
import dev.cire.server.configureSerialization
import dev.cire.server.configureSockets
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureSerialization()
    configureRouting()
}
