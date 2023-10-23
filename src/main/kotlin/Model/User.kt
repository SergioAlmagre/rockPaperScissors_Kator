package Model

import kotlinx.serialization.Serializable

@Serializable
open class User{
    val userId:Int?
    val userName:String?
    val password:String?
    val rol:Int?

    constructor(userId: Int?, userName: String?, password: String?, rol: Int?) {
        this.userId = userId
        this.userName = userName
        this.password = password
        this.rol = rol
    }

    constructor() {
        userId = null
        userName = null
        password = null
        rol = null
    }


}
