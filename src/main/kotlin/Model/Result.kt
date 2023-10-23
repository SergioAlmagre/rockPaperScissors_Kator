package Model

import kotlinx.serialization.Serializable

@Serializable
data class Result(val gameNumber:Int, val playerWinnerId:Int, val gameOver:Int)