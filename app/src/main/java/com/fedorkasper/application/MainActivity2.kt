package com.fedorkasper.application

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.fedorkasper.application.databinding.ActivityMain2Binding
import com.fedorkasper.application.databinding.CardPostBinding
import com.fedorkasper.application.fragments.ListPostFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity2 = this
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        binding.bottomNavigationView.setupWithNavController(navController)

           // isStartWhitShare() // Проверяет как запущено приложение,
            // обычно или с помощью поделиться другого приложения

        val preferences = getPreferences(Context.MODE_PRIVATE)
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
//            val fragment: ListPostFragment =
//                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as ListPostFragment
//            if (fragment != null) fragment.appPost(text)
//            else
//                Snackbar.make(binding.root, "fragment не найден", BaseTransientBottomBar.LENGTH_INDEFINITE)
//                    .show()
            //listPostFragment.appPost(text) // Добавляю новый пост, с полученным сообщением
            it.action = Intent.ACTION_MAIN //Обнуляю статус передач, и НЕ закрываю приложене
        }
///////////////////////////////////////////////////////////////////////
    }
}