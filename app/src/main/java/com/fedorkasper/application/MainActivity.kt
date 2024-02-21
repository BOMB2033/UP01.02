package com.fedorkasper.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button1).setOnClickListener{
            startActivity(Intent(this,MainActivity2::class.java))
            Toast.makeText(this,R.string.happened_next, Toast.LENGTH_LONG).show()
            finish()
        }

    }
}