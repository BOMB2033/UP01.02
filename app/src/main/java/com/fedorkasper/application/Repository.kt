package com.fedorkasper.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id:Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var posts = listOf(
        Post(
        id = 1,
        header = "Заголовок первого поста",
        content = "Первый пост",
        dateTime = "01 январь 2024 в 59:59",
        isLike = false,
        amountLikes = 999,
        amountShares = 999
    ),
        Post(
            id = 2,
            header = "Заголовок второго поста",
            content = "Второй пост",
            dateTime = "01 январь 2024 в 59:59",
            isLike = false,
            amountLikes = 999,
            amountShares = 999
        ))
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
}
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    fun likeById(id:Int) = repository.likeById(id)
    fun shareById(id:Int) = repository.shareById(id)
    fun removeById(id:Int) = repository.removeById(id)
}