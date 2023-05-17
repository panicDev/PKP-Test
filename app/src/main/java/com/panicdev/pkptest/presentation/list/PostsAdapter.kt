package com.panicdev.pkptest.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.panicdev.pkptest.databinding.PostListItemBinding
import com.panicdev.pkptest.domain.entity.Post

class PostsAdapter(
    private var data: ArrayList<Post> = ArrayList(),
    private val onClick: (post: Post) -> Unit,
    private val onEditPost: (post: Post, position: Int) -> Unit,
    private val onDeletePost: (post: Post, position: Int) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val postItemBinding = PostListItemBinding.inflate(
            layoutInflater,
            parent, false
        )

        return PostsViewHolder(postItemBinding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) =
        holder.bind(data[position])

    @SuppressLint("NotifyDataSetChanged")
    fun swapData(data: ArrayList<Post>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addPost(post: Post) {
        this.data.add(post)
        notifyItemInserted(itemCount)
    }

    fun deletePost(delPosition: Int) {
        this.data.removeAt(delPosition)
        notifyItemRemoved(delPosition)
    }

    inner class PostsViewHolder(var itemViewBinding: PostListItemBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(item: Post) = run {
            itemViewBinding.apply {
                postTitle.text = item.title
                postBody.text = item.body

                root.setOnClickListener { onClick(item) }
                btnEdit.setOnClickListener { onEditPost(item, bindingAdapterPosition) }
                btnDelete.setOnClickListener { onDeletePost(item, bindingAdapterPosition) }
                return@run
            }
        }
    }
}
