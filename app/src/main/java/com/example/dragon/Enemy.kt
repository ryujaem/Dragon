package com.example.dragon

import android.util.Log

class Enemy {
    var posX = 0f
    var posY = 0f
    var speedX = 0
    var speedY = 0
    var bWidth = 0f
    var bheight = 0f
    var HP = 0
    var rank = 0
    constructor(posX: Float,posY:Float,speedX:Int,speedY:Int,bWidth:Float,bheight:Float,Rank : Int){
        this.posX = posX
        this.posY = posY
        this.speedX = speedX
        this.speedY = speedY
        this.bWidth = bWidth
        this.bheight = bheight
        this.rank = Rank
        if(Rank == 1){
            this.HP = 500
        }else if(Rank == 3){
            this.HP = 20000
        }else{
            this.HP = 600
        }
    }

}