package  com.fedorkasper.application
data class Post(
    val id:Int,
    val header:String,
    val content:String,
    val dateTime:String,
    var amountLikes:Int,
    var amountShares:Int,
    var isLike:Boolean = false
)