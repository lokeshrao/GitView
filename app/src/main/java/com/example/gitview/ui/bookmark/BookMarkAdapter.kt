package com.example.gitview.ui.bookmark

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitview.R
import com.example.gitview.data.models.SearchUser
import com.example.gitview.databinding.UserSearchItemBinding
import com.example.gitview.utils.BookmarkPrefs
import com.example.gitview.utils.ImageLoader

class BookmarkAdapter(
    private val bookmarkPrefs: BookmarkPrefs, private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    private val searchUserList = mutableListOf<SearchUser>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(searchUsers: List<SearchUser>) {
        searchUserList.clear()
        searchUserList.addAll(searchUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding =
            UserSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun getItemCount(): Int = searchUserList.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(searchUserList[position], position)
    }

    inner class BookmarkViewHolder(private val binding: UserSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchUser: SearchUser, position: Int) {
            binding.textViewUserName.text = searchUser.login
            ImageLoader.loadImage(searchUser.avatarUrl, binding.imageViewUser)

            binding.imageViewBookmark.setImageResource(R.drawable.bookmark_24px_fill)

            binding.imageViewBookmark.setOnClickListener {
                bookmarkPrefs.removeBookmark(searchUser.login)
                searchUserList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, searchUserList.size)
            }

            binding.root.setOnClickListener {
                onItemClick(searchUser.login)
            }
        }
    }
}
