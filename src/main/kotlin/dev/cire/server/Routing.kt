package dev.cire.server

import dev.cire.controller.solanaController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        solanaController()
    }
}
