package com.fedorkasper.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Calendar
import kotlin.random.Random

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id:Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun addPost(post: Post,string: String)
    fun editById(id: Int, header: String, content: String, url:String)
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = getPosts()
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id:Int) {

        posts = posts.map{
            if(it.id != id) it else{
                if (it.isLike)
                    it.amountLikes--
                else
                    it.amountLikes++
                it.copy(isLike = !it.isLike)
            }
        }
        data.value = posts
    }
    override fun shareById(id:Int) {
        posts = posts.map {
            if(it.id != id)
                it
            else
                it.copy(amountShares = it.amountShares + 10)
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts

    }

    override fun addPost(post: Post,string: String) {
        posts = listOf(
            post.copy(
                id = 0,
                dateTime = Calendar.getInstance().time,
                content = string,
                amountLikes = randomNumb(),
                amountShares = randomNumb(),
                amountViews = randomNumb(),
                isLike = false
            )
        ) + posts
        data.value = posts
    }

    override fun editById(id: Int, header: String, content: String, url: String) {
        posts = posts.map {
            if(it.id != id)
                it
            else {
                if (it.id == 0 ) it.id = nextId(posts)
                it.copy(header = header, content = content, url = url)
            }
        }
        data.value = posts
    }
}


class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    private val edited = MutableLiveData(getEmptyPost())
    fun addPost(string: String){
        edited.value?.let {
            repository.addPost(it,string)
        }
            edited.value = getEmptyPost()
    }
    fun editById(id: Int,header:String,content:String,url:String){
        repository.editById(id,header,content,url)
    }
    fun likeById(id:Int) = repository.likeById(id)
    fun shareById(id:Int) = repository.shareById(id)
    fun removeById(id:Int) = repository.removeById(id)
}
fun nextId(posts:List<Post>):Int{
    var id = 1
    posts.forEach{ _ ->
        posts.forEach{
            if (it.id==id) id=it.id+1
        }
    }

    return id
}

fun randomNumb():Int
{
    return when (Random.nextInt(1,3))
    {
        1 -> Random.nextInt(0,1_000)
        2 -> Random.nextInt(1_000,1_000_000)
        3 -> Random.nextInt(1_000_000,1_000_000_000)
        else -> Random.nextInt(0, Int.MAX_VALUE)
    }
}