package  com.fedorkasper.application
data class Post(
    val id:Long,
    val header:String,
    val content:String,
    val dateTime:String,
    var amountLikes:Int,
    var amountShare:Int,
    var isLike:Boolean = false
)