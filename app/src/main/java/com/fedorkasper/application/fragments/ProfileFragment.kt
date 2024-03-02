package com.fedorkasper.application.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.fedorkasper.application.Post
import com.fedorkasper.application.PostAdapter
import com.fedorkasper.application.R
import com.fedorkasper.application.databinding.CardPostBinding
import com.fedorkasper.application.databinding.FragmentProfileBinding
import com.fedorkasper.application.listPostFragment
import com.fedorkasper.application.mainActivity2
import com.fedorkasper.application.profileFragment
import java.io.File
class ProfileFragment : Fragment(), PostAdapter.Listener {
    private lateinit var binding:FragmentProfileBinding
       override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
           binding = FragmentProfileBinding.inflate(layoutInflater)
           return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFragment = this


        val adapter = PostAdapter(this) // Передаю MainActivity2, которая представленная как PostAdapter.Listener
        // Для того что бы adapter мог вызывать функции которые написаны ниже
        binding.container.adapter = adapter // Передаю адаптер нашему RecyclerView,
         listPostFragment.postViewModel.data.observe(viewLifecycleOwner){  // observe - следит за изменениями
            // Если произошли изменения в postViewModel.data,
            // то будет происходить действие ниже
            adapter.submitList(it.filter { it.author == "Kasper" }) // ЛОЛОЛ ТРУ ляля Завтра грабим короля
        }
    }

    override fun onClickLike(post: Post) {
        listPostFragment.postViewModel.likeById(post.id)
    }
    override fun onClickShare(post: Post) {
        listPostFragment.postViewModel.shareById(post.id)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,post.author+"\n"+post.content+"\n"+post.url)
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
                R.id.menu_item_delete -> listPostFragment.postViewModel.removeById(post.id) //Удаление
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

            editTextContentURL.visibility = View.GONE
            textViewContentURL.visibility = View.VISIBLE

            constraintEdit.visibility = View.GONE
            constraintLayoutLikeShareSees.visibility = View.VISIBLE


        }
    }
    override fun saveEditPost(post: Post, binding: CardPostBinding) {
        with(binding)
        {
            listPostFragment.postViewModel.editById(
                post.id,
                binding.editTextHeader.text.toString(),
                binding.editTextContent.text.toString(),
                binding.editTextContentURL.text.toString()
            )
        }

        cancelEditPost(post, binding)
    }

}