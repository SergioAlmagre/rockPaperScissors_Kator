package Rutes

import BD.StaticConextion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import Model.Answer
import Model.User

private val users = StaticConextion.getUsersArrayList()

fun Route.userRouting(){  //Esta ruta se incluirá en el archivo Routing.
        route("/list") {
            get {
                if (users.isNotEmpty()) {
                    call.respond(users)
                } else {
                    call.respondText("There aren't users", status = HttpStatusCode.OK)
                }
            }
            get("{userId?}") {
                val userId = call.parameters["userId"] ?: return@get call.respondText(
                    "id empty in the url",
                    status = HttpStatusCode.BadRequest
                )

                try {
                    val idUserStr = userId.toString()
                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.BadRequest)
                    return@get call.respond(Answer("Parameter ${userId} not valid", HttpStatusCode.BadRequest.value))
                }
                val usuario = users.find { it.userId.toString() == userId }
                if (usuario == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@get call.respond(Answer("User ${userId} not found", HttpStatusCode.NotFound.value))
                    //call.respondText("Usuario ${id} no encontrado", status = HttpStatusCode.NotFound)
                }
                call.respond(usuario)
            }
        }
        route("/login") {
            post{
                val us = call.receive<User>()
                val user = StaticConextion.login(us.userName,us.password)
//                val user = users.find { it.userName == us.userName && it.password == us.password }
//                println(user.toString())
                if (user == null) {
                    call.response.status(HttpStatusCode.NotFound)
                    return@post call.respond(Answer("User ${us.userName} wrong login", HttpStatusCode.NotFound.value))
                    //call.respondText("User ${us.userName} not found", status = HttpStatusCode.NotFound)
                }
                call.respond(user)
            }
        }
        route("/register") {
            post{
                val us = call.receive<User>()
                users.add(us)
                for(i in users){
                    println(i)
                }
                StaticConextion.addUser(us)
                call.respondText("User created",status = HttpStatusCode.Created)
            }
        }
        route("/delete") {
            delete("{userId?}") {
                val idUserStr = call.parameters["userId"] ?: return@delete call.respondText("Id empty in the url", status = HttpStatusCode.BadRequest)
                if (users.removeIf { it.userId.toString() == idUserStr }){
                    StaticConextion.deleteUser(call.parameters["userId"]!!.toInt())
                    call.respondText("User removed",status = HttpStatusCode.Accepted)
                }
                else {
                    call.respondText("Not found",status = HttpStatusCode.NotFound)
                }
            }
        }
        route("/modify") {
            put("{userId?}") {
                val idUserStr = call.parameters["userId"] ?: return@put call.respondText("Id empty in the url", status = HttpStatusCode.BadRequest)
                val us = call.receive<User>()
                val pos = users.indexOfFirst{ it.userId.toString() == idUserStr}
                StaticConextion.modifyUser(us)
                if (pos == -1){
                    call.respondText("Not found",status = HttpStatusCode.NotFound)
                }
                else {
                    users[pos] = us
                    call.respondText("User modified",status = HttpStatusCode.Accepted)
                }
            }
        }




}