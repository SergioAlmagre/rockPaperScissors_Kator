package Model

import kotlinx.serialization.Serializable

@Serializable
data class Answer(val message:String, val status:Int)
