package com.fedorkasper.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import com.fedorkasper.application.databinding.ActivityMain2Binding
import com.fedorkasper.application.databinding.LayoutPostAddBinding


class MainActivity2 : AppCompatActivity(),PostAdapter.Listener {
    private val postViewModel: PostViewModel by viewModels()
    private val layoutViewModel:LayoutViewModel by viewModels()
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        val bindingAddPost = LayoutPostAddBinding.inflate((layoutInflater))
        setContentView(if(layoutViewModel.isFirst) binding.root else bindingAddPost.root )

        binding.buttonAddPost.setOnClickListener {

            layoutViewModel.isFirst = false
                setContentView(if(layoutViewModel.isFirst) binding.root else bindingAddPost.root )
                with(bindingAddPost)
                {
                    buttonCancel.setOnClickListener {

                        editTextHeaderAddPost.setText("")
                        editTextContentAddPost.setText("")

                        editTextHeaderAddPost.clearFocus()
                        editTextContentAddPost.clearFocus()

                        layoutViewModel.isFirst = true
                        setContentView(if(layoutViewModel.isFirst) binding.root else bindingAddPost.root )

                    }
                    buttonCreate.setOnClickListener {
                        if (editTextHeaderAddPost.text.isNullOrBlank() || editTextContentAddPost.text.isNullOrBlank()) return@setOnClickListener
                        postViewModel.changeContent(
                            editTextHeaderAddPost.text.toString(),
                            editTextContentAddPost.text.toString()
                        )
                        postViewModel.save()

                        editTextHeaderAddPost.setText("")
                        editTextContentAddPost.setText("")

                        editTextHeaderAddPost.clearFocus()
                        editTextContentAddPost.clearFocus()

                        (editTextHeaderAddPost.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            editTextHeaderAddPost.windowToken,0)
                        (editTextContentAddPost.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                            editTextContentAddPost.windowToken,0)

                        layoutViewModel.isFirst = true
                        setContentView(if (layoutViewModel.isFirst) binding.root else root)

                    }
                }


        }

        val adapter = PostAdapter(this)
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
    override fun onClickMore(post:Post, view: View) {
        val popupMenu = PopupMenu(this,view)
        popupMenu.inflate(R.menu.test)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_delete) {
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show()
                postViewModel.removeById(post.id)
            }
            true
        }
        popupMenu.show()
    }
}