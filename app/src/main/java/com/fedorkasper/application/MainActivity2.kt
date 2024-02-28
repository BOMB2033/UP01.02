package com.fedorkasper.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.view.get
import com.fedorkasper.application.databinding.ActivityMain2Binding
import com.fedorkasper.application.databinding.CardPostBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class MainActivity2 : AppCompatActivity(),PostAdapter.Listener {
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)
        intent?.let {
            if (it.action != Intent.ACTION_SEND){
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if(text.isNullOrBlank())
            {
                Snackbar.make(binding.root, "Пусто лол", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("Окей"){
                        finish()
                    }.show()
                return@let
            }
            postViewModel.addPost(text)
            it.action = ""
        }
        val adapter = PostAdapter(this)
        binding.buttonAddPost.setOnClickListener{
            postViewModel.addPost("")

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

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,post.content)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, "Поделиться")
        startActivity(shareIntent)

    }
    override fun onClickMore(post:Post, view: View, binding: CardPostBinding) { //Собыите нажатие на кнопку меню

        val popupMenu = PopupMenu(this,view) //Объявление объекта меню

        popupMenu.inflate(R.menu.popup_menu_post) //Указываю на каком лайоуте она будет показываться

        popupMenu.setOnMenuItemClickListener { //Слушатель нажиманий на итемы
            when(it.itemId)
            {
                R.id.menu_item_delete -> postViewModel.removeById(post.id) //Удаление
                R.id.menu_item_edit -> editModeOn(binding,"") //Редактирование
            }
            true //Просто хз, ноу комент

        }

        popupMenu.show() // Показываю менюшку

    }
    override fun editModeOn(binding: CardPostBinding,content:String) {
        with(binding)
        {
            editTextHeader.visibility = View.VISIBLE
            textViewHeader.visibility = View.INVISIBLE

            if (content!="")
                editTextContent.setText(content)
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