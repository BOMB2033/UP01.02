package com.fedorkasper.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    private var isLike = false
    private var amountLike = 999
    private var amountShare = 999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val buttonLike = findViewById<ImageButton>(R.id.buttonLike)
        val buttonShare = findViewById<ImageButton>(R.id.buttonShare)
        val textViewAmountLike = findViewById<TextView>(R.id.textViewAmountLike)
        val textViewAmountShare = findViewById<TextView>(R.id.textViewAmountShare)

        buttonLike.setOnClickListener {
            if(isLike) {
                amountLike--
                buttonLike.setImageResource(R.drawable.heart_unpress)
            } else {
                amountLike++
                buttonLike.setImageResource(R.drawable.heart_press)
            }
            textViewAmountLike.text = convertToString(amountLike)
            isLike = isLike.not()
        }

        buttonShare.setOnClickListener {
            amountShare+=100
            textViewAmountShare.text = convertToString(amountShare)
        }
    }

    private fun convertToString(count:Int):String{
        return when(count){
            in 0..<1_000 -> count.toString()
            in 1_000..<1_000_000 -> ((count/100).toFloat()/10).toString() + "K"
            in 1_000_000..<1_000_000_000 -> ((count/100_000).toFloat()/10).toString() + "M"
            else -> getString(R.string.more_billion)
        }
    }
}