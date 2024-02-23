package com.fedorkasper.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        header = "HEADER header",
        content = "text text text text text text text text text " +
                "text text text text text text text text text text " +
                "text text text text text text text text text text " +
                "text text text text text text text text text text " +
                "text text text text text text text text text text " +
                "text text text text text text text text text text text",
        dateTime = "01 январь 2024 в 59:59",
        isLike = false,
        amountLikes = 999,
        amountShares = 999
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        if (post.isLike)
            post.amountLikes--
        else
            post.amountLikes++
        post = post.copy(isLike = !post.isLike)
        data.value = post
    }
    override fun share() {
            post.amountShares+=10
        data.value = post
    }
}
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
}