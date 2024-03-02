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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.fedorkasper.application.databinding.FragmentProfileBinding
import com.fedorkasper.application.mainActivity2
import java.io.File


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
       override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
           binding = FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        binding.imageButtonAvatar.setImageURI(uri)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButtonAvatar.setOnClickListener {
            getContent.launch("image/*")
        }
    }
    companion object {
        val REQUEST_TAKE_PHOTO = 0
        val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     //   binding.photo.setImageURI(data?.data)
    }
    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(mainActivity2.packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(mainActivity2.packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }

}