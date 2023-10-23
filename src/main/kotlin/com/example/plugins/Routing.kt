package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import Model.Answer
import Rutes.gameRouting
import Rutes.userRouting

fun Application.configureRouting() {
    routing {
        get("/") {
            //call.respondText("Hello World!")
            call.response.status(HttpStatusCode.OK)
            call.respond(Answer("Server is working!", HttpStatusCode.OK.value))
        }
        userRouting()
        gameRouting()
    }
}
