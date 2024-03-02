package com.fedorkasper.application.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fedorkasper.application.MainActivity2
import com.fedorkasper.application.Post
import com.fedorkasper.application.PostAdapter
import com.fedorkasper.application.PostViewModel
import com.fedorkasper.application.R
import com.fedorkasper.application.databinding.CardPostBinding
import com.fedorkasper.application.databinding.FragmentListPostBinding
import com.fedorkasper.application.mainActivity2
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ListPostFragment : Fragment(), PostAdapter.Listener {
    private lateinit var binding: FragmentListPostBinding
    val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment\
        binding = FragmentListPostBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isStartWhitShare()



        run {
            val preferences = requireContext().getSharedPreferences("repo",Context.MODE_PRIVATE)
            if(preferences.getString("appName", "")=="")
                preferences.edit().apply{
                    putString("appName","Kasper")
                    commit()
                }
            binding.textViewHeaderApp.text = preferences?.getString("appName", "")
        }



        val adapter = PostAdapter(this) // Передаю MainActivity2, которая представленная как PostAdapter.Listener
        // Для того что бы adapter мог вызывать функции которые написаны ниже

        binding.buttonAddPost.setOnClickListener{ // Кнопка добавления поста
            postViewModel.addPost("") // Добавляю пустой пост
            binding.container.smoothScrollToPosition(-0) // Прокручиваю скролл в самый вверх
        }

        binding.container.adapter = adapter // Передаю адаптер нашему RecyclerView,

        postViewModel.data.observe(viewLifecycleOwner){  // observe - следит за изменениями
            // Если произошли изменения в postViewModel.data,
            // то будет происходить действие ниже
            adapter.submitList(it) // ЛОЛОЛ ТРУ ляля Завтра грабим короля
        }
    }

    private fun isStartWhitShare(){
        /////  Если приложение запускается через передачу текстового сообщений
        mainActivity2.intent?.let {
            if (it.action != Intent.ACTION_SEND) // Если запуск произведён стандартно (ACTION_MAIN)
                return@let                        // То пропускаем нижний код

            val text = it.getStringExtra(Intent.EXTRA_TEXT) // Получаем текст с которым поделились
            if(text.isNullOrBlank()) //Если текста нет или "", то выводим сообщение и прерываемся
            {
                Snackbar.make(binding.root, "Пусто лол", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("Окей"){
                        mainActivity2.finish()
                    }.show()
                return@let
            }
//            val fragment: ListPostFragment =
//                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as ListPostFragment
//            if (fragment != null) fragment.appPost(text)
//            else
//                Snackbar.make(binding.root, "fragment не найден", BaseTransientBottomBar.LENGTH_INDEFINITE)
//                    .show()
            postViewModel.addPost(text) // Добавляю новый пост, с полученным сообщением
            it.action = Intent.ACTION_MAIN //Обнуляю статус передач, и НЕ закрываю приложене
        }
///////////////////////////////////////////////////////////////////////
    }
    fun appPost(string: String){
        postViewModel.addPost(string)
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

        val popupMenu = PopupMenu(context,view) //Объявление объекта меню

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

        cancelEditPost(post, binding)
    }

}