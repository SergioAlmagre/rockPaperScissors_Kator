package BD

import Model.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.sql.*
import java.util.*


object StaticConextion {
    // ********************* Atributos *************************
    var conexion: Connection? = null

    // Atributo a través del cual hacemos la conexión física.
    var sentenciaSQL: Statement? = null

    // Atributo que nos permite ejecutar una sentencia SQL
    var registros: ResultSet? = null

    // ----------------------------------------------------------
    fun openConnection(): Int {
        var cod = 0
        try {

            // Cargar el driver/controlador JDBC de MySql
            val controlador = "com.mysql.cj.jdbc.Driver"
            val URL_BD = "jdbc:mysql://" + Constants.server+":"+Constants.port+"/" + Constants.bbdd


            Class.forName(controlador)

            // Realizamos la conexión a una BD con un usuario y una clave.
            conexion = DriverManager.getConnection(URL_BD, Constants.user, Constants.passwd)
            sentenciaSQL = StaticConextion.conexion!!.createStatement()
            println("Conexion realizada con éxito")
        } catch (e: Exception) {
            System.err.println("Exception: " + e.message)
            cod = -1
        }
        return cod
    }

    // ------------------------------------------------------
    fun closeConnection(): Int {
        var cod = 0
        try {
            conexion!!.close()
            println("Desconectado de la Base de Datos") // Opcional para seguridad
        } catch (ex: SQLException) {
            cod = -1
        }
        return cod
    }

    // ---------------------------------------------------------
    fun login(userName: String?, password: String?): User? {
        var p: User? = null
        println(userName + password)
        try {
            openConnection()
            val sentence = "SELECT * FROM " + Constants.userTable + " WHERE userName = ? AND password = ?"
            val pstmt = conexion!!.prepareStatement(sentence)
            // pstmt.setInt(1, 30000);
            pstmt.setString(1, userName)
            pstmt.setString(2, password)
            registros = pstmt.executeQuery()
            while (StaticConextion.registros!!.next()) {
                p = User(
                    StaticConextion.registros!!.getInt("userId"),
                    StaticConextion.registros!!.getString("userName"),
                    StaticConextion.registros!!.getString("password"),
                    StaticConextion.registros!!.getInt("rol")
                )
            }
            println(p)
        } catch (ex: SQLException) {
        } finally {
            closeConnection()
        }
        return p
    }

    // ----------------------------------------------------------
    fun getUsersArrayList(): ArrayList<User> {
        val lp: ArrayList<User> = ArrayList(1)
        try {
            openConnection()
            val sentencia = "SELECT * FROM " + Constants.userTable
            registros = sentenciaSQL!!.executeQuery(sentencia)
            while (StaticConextion.registros!!.next()) {
                lp.add(
                    User(
                        StaticConextion.registros!!.getInt("userId"),
                        StaticConextion.registros!!.getString("userName"),
                        StaticConextion.registros!!.getString("password"),
                        StaticConextion.registros!!.getInt("rol")
                    )
                )
            }
        } catch (ex: SQLException) {
        } finally {
            closeConnection()
        }
        return lp
    }


    // ----------------------------------------------------------
    fun modifyUser(user: User): Int {
        var hoyMany = 0
        println("Nuevo usuario " + user.toString())
        try {
            openConnection()
            val sentencia = "UPDATE ${Constants.userTable} SET userName = ?, password = ?, rol = ? WHERE userId = ?"

            val pstmt = conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, user.userName)
            pstmt.setString(2, user.password)
            pstmt.setInt(3, user.rol!!)
            pstmt.setInt(4,user.userId!!)
            hoyMany = pstmt.executeUpdate()
        } catch (ex: SQLException) {
            println(ex.message)
        } finally {
            closeConnection()
        }
        return hoyMany
    }


    // ----------------------------------------------------------
    fun addUser( user:User): Int {
        var cod = 0
        val sentence = "INSERT INTO " + Constants.userTable + " VALUES (default, ?, ?, ?)"
        try {
            openConnection()
            val pstmt = conexion!!.prepareStatement(sentence)
            pstmt.setString(1, user.userName)
            pstmt.setString(2, user.password)
            pstmt.setInt(3, user.rol!!)
            pstmt.executeUpdate()
        } catch (sq: SQLException) {
            cod = sq.errorCode
        } finally {
            closeConnection()
        }
        return cod
    }

    // ----------------------------------------------------------
    fun deleteUser(userId: Int): Int {
        var howMany = 0
        println("idUser" + userId)
        val sentence = "DELETE FROM " + Constants.userTable + " WHERE userId = ?"
        try {
            openConnection()
            val pstmt = conexion!!.prepareStatement(sentence)
            pstmt.setInt(1, userId)
            howMany = pstmt.executeUpdate()
        } catch (ex: SQLException) {
        } finally {
            closeConnection()
        }
        println(howMany)
        return howMany
    }



    // ------------------------------------------------------
    fun deleteUsers(): Int {
        val sentencia = "TRUNCATE " + Constants.userTable
        var cod = 0
        try {
            openConnection()
            sentenciaSQL!!.executeUpdate(sentencia)
        } catch (ex: SQLException) {
            cod = ex.errorCode
        } finally {
            closeConnection()
        }
        return cod
    }

    fun getUser(userId:Int): User {
        var user:User? = null
        try {
            openConnection()
            val sentence = "SELECT * FROM ${Constants.userTable} where userId = ?"
            val pstmt = conexion!!.prepareStatement(sentence)
            pstmt.setInt(1, userId)

            if (registros!!.next()) {
                user = User(
                    registros!!.getInt(1),
                    registros!!.getString(2),
                    registros!!.getString(3),
                    registros!!.getInt(4))
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            closeConnection()
        }
        return user!!
    }



   /** ------------------------- GAME ----------------------------------------**/

   fun getGamesArrayList(): ArrayList<Game> {
       val lp: ArrayList<Game> = ArrayList(1)
       try {
           openConnection()
           val sentencia = "SELECT * FROM " + Constants.gameTable
           registros = sentenciaSQL!!.executeQuery(sentencia)
           while (StaticConextion.registros!!.next()) {
               lp.add(
                   Game(
                       StaticConextion.registros!!.getInt("gameId"),
                       StaticConextion.registros!!.getInt("gameNumber"),
                       StaticConextion.registros!!.getInt("playerId"),
                       StaticConextion.registros!!.getInt("idHand"),
                       StaticConextion.registros!!.getInt("handWon"),
                   )
               )
           }
       } catch (ex: SQLException) {
       } finally {
           closeConnection()
       }
       return lp
   }


    fun addGame( game:Game): Int {
        var cod = 0
        val sentence = "INSERT INTO " + Constants.gameTable + " VALUES (default, ?, ?, ?, ?)"
        try {
            openConnection()
            val pstmt = conexion!!.prepareStatement(sentence)

            pstmt.setInt(1, game.gameNumber)
            pstmt.setInt(2, game.playerId)
            pstmt.setInt(3, game.idHand)
            pstmt.setInt(4, game.handWon)
            pstmt.executeUpdate()
        } catch (sq: SQLException) {
            cod = sq.errorCode
        } finally {
            closeConnection()
        }
        return cod
    }

    fun getResultsArrayList(): ArrayList<Result> {
        val re: ArrayList<Result> = ArrayList(1)
        try {
            openConnection()
            val sentencia = "SELECT * FROM " + Constants.resultTable
            registros = sentenciaSQL!!.executeQuery(sentencia)
            while (StaticConextion.registros!!.next()) {
                re.add(
                    Result(
                        StaticConextion.registros!!.getInt("gameId"),
                        StaticConextion.registros!!.getInt("winPlayerId"),
                        StaticConextion.registros!!.getInt("gameOver")
                    )
                )
            }
        } catch (ex: SQLException) {
        } finally {
            closeConnection()
        }
        return re
    }

    fun addResult( result: Result): Int {
        var cod = 0
        val sentence = "INSERT INTO " + Constants.resultTable + " VALUES (default, ?, ?)"
        try {
            openConnection()
            val pstmt = conexion!!.prepareStatement(sentence)
            pstmt.setInt(1, result.playerWinnerId)
            pstmt.setInt(2, result.gameOver)
            pstmt.executeUpdate()
        } catch (sq: SQLException) {
            cod = sq.errorCode
        } finally {
            closeConnection()
        }
        return cod
    }

    fun getMaxGameId(): Int {
        var max = 0
        try {
            openConnection()
            val sentencia = "SELECT MAX(gameNumber) FROM " + Constants.gameTable
            registros = sentenciaSQL!!.executeQuery(sentencia)
            if (registros!!.next()) {
                max = registros!!.getInt(1)
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            closeConnection()
        }
        println("Max game Number = $max")
        return max
    }

    fun recoveryGame(playerId:Int): RecoveryGame {

        var usersId = ArrayList<Int>()
        var users = ArrayList<User>()
        var players = ArrayList<Player>()
        var minGameNumber = 0
        var player: Player? = null
        var recoveryGame: RecoveryGame? = null

        try {
            openConnection()
            var sentencia = "SELECT MIN(gameNumber) from " + Constants.resultTable + " where playerId = ? and gameOver like 0"
            val pstmt = conexion!!.prepareStatement(sentencia)

            pstmt.setInt(1, playerId)

            registros = sentenciaSQL!!.executeQuery(sentencia)
            if (registros!!.next()) {
                minGameNumber = registros!!.getInt(1)
            }

            if(minGameNumber != 0){
                sentencia = "SELECT distinct (playerId) FROM " + Constants.gameTable + "where gameNumber like ${minGameNumber}"
                registros = sentenciaSQL!!.executeQuery(sentencia)
                while (StaticConextion.registros!!.next()) {
                    usersId.add(
                        (StaticConextion.registros!!.getInt(1))
                    )
                }
                for (i in usersId){
                    users.add(getUser(i))
                }

                for (i in users){
                    sentencia = "SELECT count (handWon) FROM " + Constants.gameTable + "where gameNumber like ${minGameNumber} and userId like ${i}"
                    registros = sentenciaSQL!!.executeQuery(sentencia)
                    if (registros!!.next()) {
                        player = Player(i.userId!!,i.userName,i.password,i.rol,registros!!.getInt(1),0,0)
                        players.add(player)
                    }
                }

                recoveryGame = RecoveryGame(players[0].userName.toString(), players[0].points, players[1].userName.toString(), players[1].points, minGameNumber)


            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            closeConnection()
        }
        return recoveryGame!!
    }



    // ********************** Métodos para manejar el cursor desde fuera sin usar
    // realmente el cursor **************************
    // ----------------------------------------------------------
    // ********************** Métodos para manejar el cursor desde fuera sin usar
    // realmente el cursor **************************
    // ----------------------------------------------------------
    /******
     * Este método tiene un abri conexión pero no un cerrar conexión porque me
     * servirá para llenar
     * el cursor y usarlo con los métodos posteriores.
     */
    fun rellenarDatosCursor(): Int {
        var cod = 0
        val sentencia = "SELECT * FROM " + Constants.userTable
        try {
            registros = sentenciaSQL!!.executeQuery(sentencia)
        } catch (ex: SQLException) {
            cod = ex.errorCode
        }
        return cod
    }

    // ----------------------------------------------------------
    fun getRegistroActual(): User? {
        var p: User? = null
        try {
            // Num_Cols = registros.getMetaData().getColumnCount();
            p = User(
                StaticConextion.registros!!.getInt("userId"),
                StaticConextion.registros!!.getString("userName"),
                StaticConextion.registros!!.getString("password"),
                StaticConextion.registros!!.getInt("rol")
            )
        } catch (ex: SQLException) {
        }
        return p
    }

    // ------------------------------------------------------
    fun obtenerPrimero(Campo: String?): User? {
        var p: User? = null
        try {
            if (registros!!.first()) {
                p = User(
                    StaticConextion.registros!!.getInt("userId"),
                    StaticConextion.registros!!.getString("userName"),
                    StaticConextion.registros!!.getString("password"),
                    StaticConextion.registros!!.getInt("rol")
                )
            }
            // valor = Conj_Registros.getString(Campo);
        } catch (ex: SQLException) {
        }
        return p
    }

    // ------------------------------------------------------
    fun obtenerUltimo(Campo: String?): User? {
        var p: User? = null
        try {
            if (registros!!.last()) {
                p = User(
                    StaticConextion.registros!!.getInt("userId"),
                    StaticConextion.registros!!.getString("userName"),
                    StaticConextion.registros!!.getString("password"),
                    StaticConextion.registros!!.getInt("rol")
                )
            }
            // valor = Conj_Registros.getString(Campo);
        } catch (ex: SQLException) {
        }
        return p
    }

    // ------------------------------------------------------
    fun irSiguiente(): Boolean {
        var conseguido = false
        try {
            conseguido = registros!!.next()
        } catch (ex: SQLException) {
        }
        return conseguido
    }

    // ------------------------------------------------------
    fun irAnterior(): Boolean {
        var conseguido = false
        try {
            conseguido = registros!!.previous()
        } catch (ex: SQLException) {
        }
        return conseguido
    }
}