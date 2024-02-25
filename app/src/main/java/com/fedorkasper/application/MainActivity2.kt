package com.fedorkasper.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
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
            adapter.submitList( posts)
        }
    }
    override fun onClickLike(post: Post) {
       viewModel.likeById(post.id)
    }
    override fun onClickShare(post: Post) {
        viewModel.shareById(post.id)
    }
    override fun onClickMore(post:Post, view: View) {
        val popupMenu = PopupMenu(this,view)
        popupMenu.inflate(R.menu.test)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_delete) {
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show()
                viewModel.removeById(post.id)
            }
            true
        }
        popupMenu.show()
    }
}