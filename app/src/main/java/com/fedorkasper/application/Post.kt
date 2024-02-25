package  com.fedorkasper.application

import java.util.Date

data class Post(
    val id:Int,
    val header:String,
    val content:String,
    val dateTime: Date,
    var amountLikes:Int,
    var amountShares:Int,
    var isLike:Boolean
)