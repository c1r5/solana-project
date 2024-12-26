package dev.cire.server

import dev.cire.solana.controller.solanaRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        solanaRouting()
    }
}
