package com.fedorkasper.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity2 : AppCompatActivity() {
    private var isLike = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val buttonLike = findViewById<ImageButton>(R.id.buttonLike)
        buttonLike.setOnClickListener {
            if(isLike) {
                isLike = isLike.not()
                buttonLike.setImageResource(R.drawable.heart_press)
            } else {
                isLike = isLike.not()
                buttonLike.setImageResource(R.drawable.heart_unpress)
            }
        }
    }
}