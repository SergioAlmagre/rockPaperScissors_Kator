package Model

import kotlinx.serialization.Serializable

@Serializable
data class RecoveryGame(var player1Name:String, var pointsPlayer1:Int, var player2Name:String,  var pointsPlayer2:Int, var gameNumber:Int) {



//    private var playersAL = players
//    private var gNumber = gameNumber
//
//    fun getNames(pos:Int):String?{
//        if(pos<this.playersAL.size){
//            return this.playersAL[pos].userName!!
//        }
//        return null
//    }
//
//    fun getId(pos:Int):Int?{
//        if(pos<this.playersAL.size){
//            return this.playersAL[pos].userId!!
//        }
//        return null
//    }
//
//    fun getPoints(pos: Int):Int?{
//        if(pos<this.playersAL.size) {
//            return this.playersAL[pos].points
//        }
//        return null
//    }



}