package com.example.dragon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var myCanvas: MyCanvas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myCanvas = findViewById(R.id.myCanvas)

        mhandler.sendEmptyMessageDelayed(0,1000)
    }

    var mhandler =  object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d("aaaa"," 실행1")
            if(myCanvas.quit == true ){

                Log.d("aaaa"," 실행2")
                var intent : Intent = Intent(baseContext, EndActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Log.d("aaaa"," 실행3")
                sendEmptyMessageDelayed(0,1000)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        myCanvas.isRunning = false
        finish()
    }
}