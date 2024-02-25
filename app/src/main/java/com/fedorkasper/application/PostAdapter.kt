package com.fedorkasper.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fedorkasper.application.databinding.CardPostBinding
class PostDiffCallback : DiffUtil.ItemCallback<Post>()
{
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
            textViewDataTime.text = post.dateTime
            textViewContent.text = post.content

            textViewAmountLike.text = convertToString(post.amountLikes)
            textViewAmountShare.text = convertToString(post.amountShares)

            buttonLike.setImageResource(if (post.isLike) R.drawable.heart_press else R.drawable.heart_unpress)
            buttonLike.setOnClickListener {
                listener.onClickLike(post)
            }
            buttonShare.setOnClickListener {
                listener.onClickShare(post)
            }
        }
    }
}
class PostAdapter(
    private val listener: Listener
):ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
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