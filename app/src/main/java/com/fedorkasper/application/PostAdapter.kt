package com.fedorkasper.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.fedorkasper.application.databinding.CardPostBinding
class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener
):RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            textViewHeader.text = post.header
            textViewDataTime.text = post.dateTime
            textViewContent.text = post.content
            buttonLike.setImageResource(if (post.isLike) R.drawable.heart_press else R.drawable.heart_unpress)
            buttonLike.setOnClickListener {
                onLikeListener(post)
            }
        }
    }
}

typealias OnLikeListener = (post:Post) -> Unit
class PostAdapter(private val onLikeListener:OnLikeListener):RecyclerView.Adapter<PostViewHolder>() {
    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position:Int){
        val post = list[position]
        holder.bind(post)
    }
    override fun getItemCount() :Int = list. size
}
