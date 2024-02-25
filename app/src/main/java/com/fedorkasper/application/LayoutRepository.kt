package com.fedorkasper.application

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.fedorkasper.application.databinding.LayoutPostAddBinding


class LayoutViewModel : ViewModel() {
    private val repository = LayoutRepositoryInMemoryImpl()
    var isFirst = repository.isFirs
    var headerText = repository.headerText
    var contentText = repository.contentText
   /* fun setLayout() = repository.setLayout()
    fun setData(appCompatActivity:AppCompatActivity,mainBinding:ViewBinding,addPostBinding:ViewBinding) = repository.setData(appCompatActivity,mainBinding,addPostBinding)
    fun checkoutLayout() = repository.checkoutLayout()*/
}

class LayoutRepositoryInMemoryImpl()  {
    val isFirs = true
    val headerText = ""
    val contentText = ""
}

/*interface LayoutRepository {
    fun setLayout()
    fun setData(appCompatActivity:AppCompatActivity,mainBinding:ViewBinding,addPostBinding:ViewBinding)
    fun checkoutLayout()
}*/
