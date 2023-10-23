package Model

class Player: User {
    var points:Int
    var playerValue:Int
    var isWinner:Int

    constructor(userId: Int?, userName: String?, password: String?, rol: Int?, points: Int, isWinner: Int, playerValue:Int) : super(
        userId,
        userName,
        password,
        rol
    ) {
        this.points = points
        this.isWinner = isWinner
        this.playerValue = playerValue
    }

    constructor(points: Int, isWinner: Int, playerValue:Int) : super() {
        this.points = points
        this.isWinner = isWinner
        this.playerValue = playerValue
    }
}