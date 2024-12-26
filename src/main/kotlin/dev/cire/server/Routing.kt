package dev.cire.server

import dev.cire.solana.controller.solanaController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        solanaController()
    }
}
