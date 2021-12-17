package com.example.dragon

class User {
    var posX = 0f
    var posY = 0f
    var speedX = 0
    var speedY = 0
    var bWidth = 0f
    var bheight = 0f
    var life = 3
    constructor(posX: Float,posY:Float,speedX:Int,speedY:Int,bWidth:Float,bheight:Float){
        this.posX = posX
        this.posY = posY
        this.speedX = speedX
        this.speedY = speedY
        this.bWidth = bWidth
        this.bheight = bheight
    }

    constructor(posX: Float,posY:Float,speedX:Int,speedY:Int,bWidth:Float,bheight:Float,life:Int){
        this.posX = posX
        this.posY = posY
        this.speedX = speedX
        this.speedY = speedY
        this.bWidth = bWidth
        this.bheight = bheight
        this.life = life
    }

}
