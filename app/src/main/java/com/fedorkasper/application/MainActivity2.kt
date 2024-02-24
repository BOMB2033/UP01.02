package com.fedorkasper.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.fedorkasper.application.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(),PostAdapter.Listener {
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(this)
        binding.container.adapter = adapter
        viewModel.data.observe(this){posts ->
            adapter.list = posts
        }
    }

    override fun onClickLike(post: Post) {
        viewModel.likeById(post.id)
    }
    override fun onClickShare(post: Post) {
        viewModel.shareById(post.id)
    }


}