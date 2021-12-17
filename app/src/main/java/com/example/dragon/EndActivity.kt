package com.example.dragon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EndActivity : AppCompatActivity() {
    lateinit var btn_back : Button
    lateinit var tv : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        tv = findViewById(R.id.tv3)
        btn_back = findViewById(R.id.btn_back)

        var point = intent.getIntExtra("point",0)
        tv.text = "$point"
        btn_back.setOnClickListener {
            var intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}