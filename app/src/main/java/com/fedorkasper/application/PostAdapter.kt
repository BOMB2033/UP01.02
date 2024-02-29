package com.fedorkasper.application

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fedorkasper.application.databinding.CardPostBinding
import com.google.android.material.snackbar.Snackbar


class PostDiffCallback : DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return  oldItem == newItem
    }

}
class PostViewHolder(private val binding: CardPostBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post,listener: PostAdapter.Listener) {
        binding.apply {

            textViewHeader.text = post.header
            editTextHeader.setText(post.header)

            textViewDataTime.text = post.dateTime.toString().split("GMT")[0]

            textViewContent.text = post.content
            editTextContent.setText(post.content)

            textViewAmountLike.text = convertToString(post.amountLikes)
            textViewAmountShare.text = convertToString(post.amountShares)
            textViewAmountViews.text = convertToString(post.amountViews)

            buttonLike.setImageResource(if (post.isLike) R.drawable.heart_press else R.drawable.heart_unpress)

            if (post.url!=""){
                Glide.with(binding.root.context)
                    .load(getURLImageVideo(post.url))
                    .into(imageViewImage)
                imageViewImage.visibility = View.VISIBLE
                imageButtonPlay.visibility = View.VISIBLE
                textViewContentURL.visibility = View.VISIBLE
                textViewContentURL.text = post.url
                editTextContentURL.setText(post.url)

                imageButtonPlay.setOnClickListener{
                    val intent = Intent()
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    if (!post.url.startsWith("https://")){
                        Snackbar.make(binding.root,"Ссылка должна начинаться с \"https://\" \n" +
                                "Так безопаснее будет",Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    intent.setData(Uri.parse(post.url))
                    binding.root.context.startActivity(intent)
                }


            }
            else{
                imageViewImage.visibility = View.GONE
                imageButtonPlay.visibility = View.GONE
                textViewContentURL.visibility = View.GONE
                editTextContentURL.setText("")

            }
            imageViewIcon.setImageResource(post.icon)

            buttonLike.setOnClickListener {
                listener.onClickLike(post)
            }
            buttonShare.setOnClickListener {
                listener.onClickShare(post)
            }
            buttonMore.setOnClickListener { //Устанавливаю слушатель на три точки
                listener.onClickMore(post,it,binding) //Вызываю функцию из MainActivity
            }
            buttonCancel.setOnClickListener {
                listener.cancelEditPost(post,binding)
            }
            buttonSave.setOnClickListener {
                listener.saveEditPost(post,binding)
            }

            if (post.id==0) listener.editModeOn(binding, "" )
        }
    }
}

@Suppress("SpellCheckingInspection")
fun getURLImageVideo(url:String):String{
    return  "https://img.youtube.com/vi/${if(url.split("v=").lastIndex == 1) url.split("v=")[1]
    else url.split("?si")[0].split("/").last()}/sddefault.jpg"

}
class PostAdapter(
    private val listener: Listener,
):ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(post, listener)
    }

    interface Listener{
        fun onClickLike(post: Post)
        fun onClickShare(post: Post)
        fun onClickMore(post:Post, view: View,binding: CardPostBinding)
        fun cancelEditPost(post:Post,binding: CardPostBinding)
        fun saveEditPost(post:Post, binding: CardPostBinding)
        fun editModeOn(binding: CardPostBinding,content:String)
    }
}
private fun convertToString(count:Int):String{
    return when(count){
        in 0..<1_000 -> count.toString()
        in 1000..<1_100-> "1K"
        in 1_100..<10_000 -> ((count/100).toFloat()/10).toString() + "K"
        in 10_000..<1_000_000 -> (count/1_000).toString() + "K"
        in 1_000_000..<1_100_000 -> "1M"
        in 1_100_000..<10_000_000 -> ((count/100_000).toFloat()/10).toString() + "M"
        in 10_000_000..<1_000_000_000 -> (count/1_000_000).toString() + "M"
        else -> "ꚙ"
    }
}