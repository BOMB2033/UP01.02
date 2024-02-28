package  com.fedorkasper.application

import java.util.Date

data class Post(
    var id:Int,
    var header:String,
    var content:String,
    val dateTime: Date,
    var amountLikes:Int,
    var amountShares:Int,
    var amountViews:Int,
    var isLike:Boolean,

)