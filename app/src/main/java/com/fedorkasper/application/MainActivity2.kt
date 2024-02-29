package com.fedorkasper.application

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import com.fedorkasper.application.databinding.ActivityMain2Binding
import com.fedorkasper.application.databinding.CardPostBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class MainActivity2 : AppCompatActivity(),PostAdapter.Listener {
    private val postViewModel: PostViewModel by viewModels()
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        isStartWhitShare() // Проверяет как запущено приложение,
        // обычно или с помощью поделиться другого приложения

        val adapter = PostAdapter(this) // Передаю MainActivity2, которая представленная как PostAdapter.Listener
                                               // Для того что бы adapter мог вызывать функции которые написаны ниже
        binding.buttonAddPost.setOnClickListener{ // Кнопка добавления поста
            postViewModel.addPost("") // Добавляю пустой пост
            binding.container.smoothScrollToPosition(-0) // Прокручиваю скролл в самый вверх
        }
        binding.container.adapter = adapter // Передаю адаптер нашему RecyclerView,
        postViewModel.data.observe(this){  // observe - следит за изменениями
            // Если произошли изменения в postViewModel.data,
            // то будет происходить действие ниже
            adapter.submitList(it) // ЛОЛОЛ ТРУ ляля Завтра грабим короля
        }

    }
    private fun isStartWhitShare(){
        /////  Если приложение запускается через передачу текстового сообщений
        intent?.let {
            if (it.action != Intent.ACTION_SEND) // Если запуск произведён стандартно (ACTION_MAIN)
                return@let                        // То пропускаем нижний код

            val text = it.getStringExtra(Intent.EXTRA_TEXT) // Получаем текст с которым поделились
            if(text.isNullOrBlank()) //Если текста нет или "", то выводим сообщение и прерываемся
            {
                Snackbar.make(binding.root, "Пусто лол", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("Окей"){
                        finish()
                    }.show()
                return@let
            }
            postViewModel.addPost(text) // Добавляю новый пост, с полученным сообщением
            it.action = Intent.ACTION_MAIN //Обнуляю статус передач, и НЕ закрываю приложене
        }
///////////////////////////////////////////////////////////////////////
    }
    override fun onClickLike(post: Post) {
       postViewModel.likeById(post.id)
    }
    override fun onClickShare(post: Post) {
        postViewModel.shareById(post.id)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,post.header+"\n"+post.content+"\n"+post.url)
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

            editTextContentURL.visibility = View.VISIBLE
            textViewContentURL.visibility = View.INVISIBLE

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

            editTextContentURL.visibility = View.INVISIBLE
            textViewContentURL.visibility = View.VISIBLE

            constraintEdit.visibility = View.GONE
            constraintLayoutLikeShareSees.visibility = View.VISIBLE
        }
        if(binding.editTextHeader.text.toString() == "" && binding.editTextContent.text.toString() == "")
            postViewModel.removeById(post.id)
    }

    override fun saveEditPost(post: Post, binding: CardPostBinding) {
        with(binding)
        {
            postViewModel.editById(
                post.id,
                binding.editTextHeader.text.toString(),
                binding.editTextContent.text.toString(),
                binding.editTextContentURL.text.toString()
            )
        }

        cancelEditPost(post,binding)
    }

}