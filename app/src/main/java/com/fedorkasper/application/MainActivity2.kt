package com.fedorkasper.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.view.get
import com.fedorkasper.application.databinding.ActivityMain2Binding
import com.fedorkasper.application.databinding.CardPostBinding


class MainActivity2 : AppCompatActivity(),PostAdapter.Listener {
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)
        val adapter = PostAdapter(this)
        binding.buttonAddPost.setOnClickListener{
            postViewModel.addPost()

            binding.container.smoothScrollToPosition(-0)
        }



        binding.container.adapter = adapter
        postViewModel.data.observe(this){ posts ->
            adapter.submitList( posts)
        }
    }
    override fun onClickLike(post: Post) {
       postViewModel.likeById(post.id)
    }
    override fun onClickShare(post: Post) {
        postViewModel.shareById(post.id)
    }
    override fun onClickMore(post:Post, view: View, binding: CardPostBinding) {

        val popupMenu = PopupMenu(this,view)

        popupMenu.inflate(R.menu.popup_menu_post)

        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_delete) {
                postViewModel.removeById(post.id)
            }
            if (it.itemId == R.id.menu_item_edit) {
                editModeOn(binding)
            }
            true
        }

        popupMenu.show()

    }
    override fun editModeOn(binding: CardPostBinding) {
        with(binding)
        {
            editTextHeader.visibility = View.VISIBLE
            textViewHeader.visibility = View.INVISIBLE

            editTextContent.visibility = View.VISIBLE
            textViewContent.visibility = View.INVISIBLE

            constraintEdit.visibility = View.VISIBLE
            constraintLayoutLikeShareSees.visibility = View.GONE
        }

    }
    override fun cancelEditPost(post: Post,binding: CardPostBinding) {
        with(binding)
        {
            editTextHeader.visibility = View.INVISIBLE
            textViewHeader.visibility = View.VISIBLE

            editTextContent.visibility = View.INVISIBLE
            textViewContent.visibility = View.VISIBLE

            constraintEdit.visibility = View.GONE
            constraintLayoutLikeShareSees.visibility = View.VISIBLE
        }
        if(binding.editTextHeader.text.toString() == "" && binding.editTextContent.text.toString() == "")
            postViewModel.removeById(post.id)
    }
    override fun saveEditPost(post: Post, binding: CardPostBinding) {
            postViewModel.editById(
                post.id,
                binding.editTextHeader.text.toString(),
                binding.editTextContent.text.toString())
        cancelEditPost(post,binding)
    }
}