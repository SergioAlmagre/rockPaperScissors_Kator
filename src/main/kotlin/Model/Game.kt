package Model

import kotlinx.serialization.Serializable

@Serializable
data class Game(val gameId:Int, val gameNumber:Int , val playerId:Int, val idHand:Int, val handWon:Int) {


}