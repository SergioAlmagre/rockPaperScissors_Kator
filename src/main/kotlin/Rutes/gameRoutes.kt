package Rutes

import BD.StaticConextion
import Model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val games = StaticConextion.getGamesArrayList()
private val results = StaticConextion.getResultsArrayList()
private val data = Data(StaticConextion.getMaxGameId())

fun Route.gameRouting(){  //Esta ruta se incluirá en el archivo Routing.

    route("/modifyGame") {
        put("{gameNumber?}") {
            val gameIdStr = call.parameters["gameNumber"] ?: return@put call.respondText("Id empty in the url", status = HttpStatusCode.BadRequest)
            val us = call.receive<Game>()
            val pos = games.indexOfFirst{ it.gameId.toString() == gameIdStr}
            if (pos == -1){
                call.respondText("Game not found",status = HttpStatusCode.NotFound)
            }
            else {
                games[pos] = us
                call.respondText("Game modified",status = HttpStatusCode.Accepted)
            }
        }
    }

    get("{gameId?}") {
        val gameId = call.parameters["gameId"] ?: return@get call.respondText(
            "id empty in the url",
            status = HttpStatusCode.BadRequest
        )

        try {
            val gameIdString = gameId.toString()
        } catch (e: Exception) {
            call.response.status(HttpStatusCode.BadRequest)
            return@get call.respond(Answer("Parameter ${gameId} not valid", HttpStatusCode.BadRequest.value))
        }
        val game = games.find { it.gameId.toString() == gameId }
        if (game == null) {
            call.response.status(HttpStatusCode.NotFound)
            return@get call.respond(Answer("Game ${gameId} not found", HttpStatusCode.NotFound.value))
            //call.respondText("Usuario ${id} no encontrado", status = HttpStatusCode.NotFound)
        }
        call.respond(game)
    }

    route("/newgame") {
        post{
            val ga = call.receive<Game>()
            games.add(ga)
            StaticConextion.addGame(ga)
            call.respondText("Game created",status = HttpStatusCode.Created)
        }
    }

    route("/newresult") {
        post{
            val re = call.receive<Result>()
            results.add(re)
            StaticConextion.addResult(re)
            call.respondText("User created",status = HttpStatusCode.Created)
        }
    }

    route("/getMaxGameId") {
        get {
            if (data.maxGameId > 0) {
                call.respond(data)
            } else {
                call.respondText("There aren't users", status = HttpStatusCode.OK)
            }
        }
    }

    route("/recoveryGame/{userId}") {
        get {
            val userId = call.parameters["userId"]?.toIntOrNull()

            if (userId != null) {
                val recoveryGame = StaticConextion.recoveryGame(userId)
                if (recoveryGame != null) {
                    call.respond(recoveryGame)
                } else {
                    call.respondText("User not found or data not available", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Invalid user ID", status = HttpStatusCode.BadRequest)
            }
        }
    }



}