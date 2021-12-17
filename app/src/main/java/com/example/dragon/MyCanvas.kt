package com.example.dragon

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.lang.Exception
import kotlin.random.Random
import android.R.attr.bitmap
import android.content.Intent

import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity


class MyCanvas : View {
    lateinit var p : Paint
    var ballArr : ArrayList<User> = arrayListOf()
    var eballArr : ArrayList<User> = arrayListOf()
    var bballArr : ArrayList<User> = arrayListOf()
    var backArr : ArrayList<User> = arrayListOf()
    var bossArr : ArrayList<Enemy> = arrayListOf()
    lateinit var user : User
    var enemyArr : ArrayList<Enemy> = arrayListOf()
    var isRunning = true
    var bitbackground = BitmapFactory.decodeResource(resources,R.drawable.background)
    var bullet = BitmapFactory.decodeResource(resources,R.drawable.bullet)
    var bullet2 = BitmapFactory.decodeResource(resources,R.drawable.bullet2)
    var userimg = BitmapFactory.decodeResource(resources,R.drawable.user)
    var boss = BitmapFactory.decodeResource(resources,R.drawable.boss)
    var heart = BitmapFactory.decodeResource(resources,R.drawable.heart)
    var sideInversion = Matrix().apply {
        setScale(1F,-1F)
    }
    var resizeHeart = Bitmap.createScaledBitmap(heart,120,120,true)
    var resizebitmap = Bitmap.createScaledBitmap(bitbackground,1080,1731,true)
    var resizeuser = Bitmap.createScaledBitmap(userimg,300,400,true)
    var enemyimg = BitmapFactory.decodeResource(resources,R.drawable.enemy1)
    var enemyimg2 = BitmapFactory.decodeResource(resources,R.drawable.enemy2)
    var resizeenemy = Bitmap.createScaledBitmap(enemyimg,300,300,true)
    var resizeenemy2 = Bitmap.createScaledBitmap(enemyimg2,300,300,true)
    var resizebossimg = Bitmap.createScaledBitmap(boss,600,650,true)
    var resizeBullet = Bitmap.createScaledBitmap(bullet,300,175,true)
    var resizeBullet2 = Bitmap.createScaledBitmap(bullet2,300,175,true)
    var bossimg = Bitmap.createBitmap(resizebossimg,0,0,resizebossimg.width,resizebossimg.height,sideInversion,true)
    var point = 0
    var quit : Boolean = false

    constructor(context: Context?) : super(context){
        p = Paint()
    }

    constructor(context: Context?, attrs:  AttributeSet?) : super(context, attrs) {
        p = Paint()
        user = User(550f, 1650f, 5, 5, 30f, 50f)
        backArr.add(User(0f,0f,1,1,0f,0f))
        addBackground()
        addEnemy()

        MyThread().start()
        //EnemyThread().start()
        //EballThread().start()
        //BackThread().start()
        //BossThread().start()
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        user.posX = event!!.x
        user.posY = event!!.y
        return true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        backArr.forEach {
            canvas.drawBitmap(resizebitmap,it.posX,it.posY,p)
        }

        when(user.life){
            1 -> canvas.drawBitmap(resizeHeart,0f,0f,null)
            2 -> {canvas.drawBitmap(resizeHeart,0f,0f,null)
                    canvas.drawBitmap(resizeHeart,130f,0f,null)}
            3 -> {canvas.drawBitmap(resizeHeart,0f,0f,null)
                    canvas.drawBitmap(resizeHeart,130f,0f,null)
                    canvas.drawBitmap(resizeHeart,250f,0f,null)}
        }

        p.setColor(Color.RED)
        synchronized(ballArr){
            for(i in 0 until ballArr.size step 1){
                var ball = ballArr[i]
                canvas.drawBitmap(resizeBullet,ball.posX-150,ball.posY,null)
            }
        }

        //user
        p.setColor(Color.BLACK)
        canvas.drawBitmap(resizeuser,user.posX-150 ,user.posY-150,p)

        synchronized(enemyArr){
            enemyArr.forEach {
                if(it.rank == 1){
                    canvas.drawBitmap(resizeenemy,it.posX-50f,it.posY,null)
                }else{
                    canvas.drawBitmap(resizeenemy2,it.posX-50f,it.posY,null)
                }
                if(it.posY == 400f){
                    addEBall(it.posX)
                }
            }
        }


        bossArr.forEach {
            p.setColor(Color.BLACK)
            canvas.drawBitmap(bossimg,it.posX,it.posY,null)
        }
        for(i in 0 until bballArr.size step 1){
            var eball = bballArr[i]
            canvas.drawBitmap(resizeBullet2,eball.posX,eball.posY+30,null)
        }

        for(i in 0 until eballArr.size step 1){
            var eball = eballArr[i]
            canvas.drawBitmap(resizeBullet2,eball.posX-50,eball.posY,null)
        }

        if(quit){
            isRunning = false
        }


        p.setColor(Color.BLUE)
        p.textSize = 140F
        canvas.drawText("점수 : $point",500f,120f,p)
    }

    fun addBackground(){
        backArr.add(User(0f,-1731f,1,1,0f,0f))
    }

    fun addEnemy(){
        for (i in 0..3) {
            var x = 350 * i
            var random = Random
            var rank = random.nextInt(2)
            enemyArr.add(Enemy(100f + x.toFloat(), -500f, 5, 1, 230f, 100f, rank))

        }
    }

    fun addBoss(){
        bossArr.add(Enemy(400f, -1200f, 1, 1, 600f, 650f, 3))
    }

    fun addBall() {

        ballArr.add(User(user.posX, user.posY, 5, 8, 10f, 25f))

    }

    fun addEBall(posX : Float){

        eballArr.add(User(posX,400f, 5, 5, 10f,25f))

    }

    fun addBBall(posX : Float,posY :Float){

        bballArr.add(User(posX+110,posY + 60, 5, 5, 10f,25f))

    }
    var key = true
    var bosstemp = 0
    //보스움직임
    inner class BossThread : Thread(){
        override fun run(){
            super.run()
            var temp = 0

            while (isRunning) {

                if(bosstemp == 4){
                    addBoss()
                    bosstemp++
                }
                if(bosstemp >= 4){
                    var i = 0
                    while (isRunning) {
                        if (i < bossArr.size) {
                            var boss = bossArr[i]

                            if (boss.posY < 250) {
                                boss.posY += boss.speedY
                            }else{
                                if(key){
                                    boss.posX -= boss.speedX
                                    if(boss.posX <= 120){
                                        key = false
                                    }
                                }else if(!key){
                                    boss.posX += boss.speedX
                                    if(boss.posX >= 700) {
                                        key = true
                                    }
                                }
                                temp ++
                                if(temp == 100){
                                    var boss = bossArr[0]
                                    addBBall(boss.posX,boss.posY)
                                    temp = 0
                                }
                                var k = 0
                                while(isRunning){
                                    if(k < bballArr.size){
                                        var bball = bballArr[k]
                                        if (bball.posY < 2200) {
                                            bball.posY += bball.speedY
                                        }else if(bball.posY > 2200){
                                            bballArr.removeAt(k)
                                            k--
                                            quit = true
                                        }
                                        var eRect: Rect = Rect(
                                            (user.posX/2+30).toInt(),
                                            user.posY.toInt(),
                                            (user.posX + user.bWidth).toInt(),
                                            (user.posY + user.bheight).toInt()
                                        )
                                        var fRect: Rect = Rect(
                                            bball.posX.toInt(),
                                            bball.posY.toInt(),
                                            (bball.posX + bball.bWidth).toInt(),
                                            (bball.posY + bball.bheight).toInt()
                                        )
                                        if (eRect.intersect(fRect)) {
                                            synchronized(bballArr) {
                                                bballArr.removeAt(k)
                                                k--
                                            }
                                            user.life--
                                        }
                                    }else{
                                        break
                                    }
                                    k++
                                }
                            }
                            if(boss.HP <= 0){
                                synchronized(bossArr){
                                    bossArr.removeAt(i)
                                    i--
                                    quit = true
                                }
                            }
                        } else {
                            break
                        }
                        i++
                    }
                }
                invalidate()
                sleep(5)
            }
        }
    }

    //적 움직임
    inner class EnemyThread : Thread(){
        override fun run(){
            super.run()
            var temp = 0
            while (isRunning) {

                    temp++
                    if (bosstemp < 5) {
                        if (temp == 800) {
                            addEnemy()
                            temp = 0
                            bosstemp++
                        }
                    }
                    var i = 0
                    while (isRunning) {
                        if (i < enemyArr.size) {
                            var enemy = enemyArr[i]
                            if(enemy.posY == 300f){
                               addEBall(enemy.posX)
                            }
                            if (enemy.posY < 2200) {
                                enemy.posY += enemy.speedY
                            }
                            if(enemy.HP <= 0){
                                synchronized(enemyArr) {
                                    enemyArr.removeAt(i)
                                    i--
                                }
                                if(enemy.rank == 1){
                                    point++
                                }else{
                                    point += 5
                                }
                            }else {
                                if (enemy.posY >= 2200) {
                                    synchronized(enemyArr) {
                                        enemyArr.removeAt(i)
                                        i--
                                    }
                                }
                            }
                        } else {
                            break
                        }
                        i++
                    }
                    invalidate()
                    sleep(5)
            }
        }
    }

    //배경
    inner class BackThread : Thread(){
        override fun run() {
            super.run()
            var bheight = 1731
            while (isRunning){
                    var i = 0
                    while (isRunning) {
                        if (i < backArr.size) {
                            var back = backArr[i]
                            if (back.posY <= bheight) {
                                back.posY += back.speedY
                            }
                            if(back.posY >= bheight){
                                backArr.removeAt(i)
                                i--
                                addBackground()
                            }
                        } else {
                            break
                        }
                        i++
                    }
                invalidate()
                sleep(5)
            }
        }
    }

    inner class EballThread : Thread(){
        override fun run(){
            super.run()
            while (isRunning) {
                var i = 0
                while (isRunning) {
                    if (i < eballArr.size) {
                        var eball = eballArr[i]
                        if (eball.posY < 2200) {
                            eball.posY += eball.speedY
                        }else if(eball.posY > 2200){
                            synchronized(eballArr){
                                eballArr.removeAt(i)
                                i--
                            }
                        }
                    } else {
                        break
                    }
                    i++
                }

                var j = 0
                while(isRunning){
                    if(j < eballArr.size){

                        var eball = eballArr[j]
                        var eRect: Rect = Rect(
                            (user.posX/2+30).toInt(),
                            user.posY.toInt(),
                            (user.posX + user.bWidth).toInt(),
                            (user.posY + user.bheight).toInt()
                        )
                        var fRect: Rect = Rect(
                            eball.posX.toInt(),
                            eball.posY.toInt(),
                            (eball.posX + eball.bWidth).toInt(),
                            (eball.posY + eball.bheight).toInt()
                        )
                        if (eRect.intersect(fRect)) {
                            synchronized(eballArr) {
                                eballArr.removeAt(j)
                                j--
                            }
                            user.life--
                        }
                    }else{
                        break
                    }
                    j++
                }
                invalidate()
                sleep(5)
            }
        }
    }

    inner class MyThread : Thread(){
        override fun run() {
            super.run()
                var temp = 0
                while (isRunning) {
                    if(user.life < 1){
                        quit = true
                    }
                    temp++
                    if (temp == 20) {
                        addBall()
                        temp = 0
                    }
                    var i = 0
                    synchronized(ballArr) {
                        while (isRunning) {
                            if (i < ballArr.size) {
                                var ball = ballArr[i]
                                if(ball.posY < 0 ){
                                    ballArr.removeAt(i)
                                    i--
                                }else{
                                    ball.posY -= ball.speedY
                                }
                            } else {
                                break
                            }
                            i++
                        }
                    }

                    var j = 0
                    while(isRunning){
                        if(j < ballArr.size){
                            var ball = ballArr[j]
                            var k = 0
                            while(isRunning) {
                                    if (k < enemyArr.size) {
                                        synchronized(enemyArr) {
                                        var enemy = enemyArr[k]

                                        var eRect: Rect = Rect(
                                            enemy.posX.toInt(),
                                            enemy.posY.toInt(),
                                            (enemy.posX + enemy.bWidth).toInt(),
                                            (enemy.posY + enemy.bheight).toInt()
                                        )
                                        var fRect: Rect = Rect(
                                            ball.posX.toInt(),
                                            ball.posY.toInt(),
                                            (ball.posX + ball.bWidth).toInt(),
                                            (ball.posY + ball.bheight).toInt()
                                        )
                                        if (eRect.intersect(fRect)) {
                                            synchronized(ballArr) {
                                                ballArr.removeAt(j)
                                                j--
                                            }
                                            if (enemy.HP > 0) {
                                                enemy.HP -= 100
                                            }
                                        }
                                    }
                                }else{
                                        break
                                    }
                                k++
                            }
                        }else{
                            break
                        }
                        j++
                    }

                    var a = 0
                    while(isRunning){
                        if(a < ballArr.size){
                            var ball = ballArr[a]
                            var b = 0
                            while(isRunning) {
                                if (b < bossArr.size) {
                                    var enemy = bossArr[b]
                                    var eRect: Rect = Rect(
                                        enemy.posX.toInt(),
                                        enemy.posY.toInt(),
                                        (enemy.posX + enemy.bWidth).toInt(),
                                        (enemy.posY + enemy.bheight).toInt()
                                    )
                                    var fRect: Rect = Rect(
                                        ball.posX.toInt(),
                                        ball.posY.toInt(),
                                        (ball.posX + ball.bWidth).toInt(),
                                        (ball.posY + ball.bheight).toInt()
                                    )
                                    if (eRect.intersect(fRect)) {
                                        synchronized(ballArr) {
                                            ballArr.removeAt(a)
                                            a--
                                        }
                                        if (enemy.HP > 0) {
                                            enemy.HP -= 100
                                        }
                                    }
                                }else{
                                    break
                                }
                                b++
                            }
                        }else{
                            break
                        }
                        a++
                    }
                    invalidate()
                    sleep(5)
                }
        }
    }
}
