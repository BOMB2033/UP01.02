package com.fedorkasper.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.fedorkasper.application.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            0,
            getText(R.string.header_post).toString(),
            getText(R.string.content_post).toString(),
            getText(R.string.dataTime_post).toString(),
            999,
            999,
            false
        )
        with(binding){
            textViewHeader.text = post.header
            textViewContent.text = post.content
            textViewDataTime.text = post.dateTime
            textViewAmountLike.text = post.amountLikes.toString()
            textViewAmountShare.text = post.amountShare.toString()

            if (post.isLike)
                buttonLike.setImageResource(R.drawable.heart_press)

            buttonLike.setOnClickListener {
                if(post.isLike) {
                    post.amountLikes--
                    buttonLike.setImageResource(R.drawable.heart_unpress)
                } else {
                    post.amountLikes++
                    buttonLike.setImageResource(R.drawable.heart_press)
                }
                textViewAmountLike.text = convertToString(post.amountLikes)
                post.isLike = post.isLike.not()
            }
            buttonShare.setOnClickListener {
                post.amountShare+=10
                textViewAmountShare.text = convertToString(post.amountShare)
            }
        }
    }

    private fun convertToString(count:Int):String{
        return when(count){
            in 0..<1_000 -> count.toString()
            in 1000..<1_100-> "1K"
            in 1_100..<10_000 -> ((count/100).toFloat()/10).toString() + "K"
            in 10_000..<1_000_000 -> (count/1_000).toString() + "K"
            in 1_000_000..<1_100_000 -> "1M"
            in 1_100_000..<10_000_000 -> ((count/100_000).toFloat()/10).toString() + "M"
            in 10_000_000..<1_000_000_000 -> (count/1_000_000).toString() + "M"
            else -> getString(R.string.more_billion)
        }
    }
}