package com.example.gitview.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitview.R
import com.example.gitview.data.models.SearchUser
import com.example.gitview.databinding.UserSearchItemBinding
import com.example.gitview.utils.BookmarkPrefs
import com.example.gitview.utils.ImageLoader

class SearchUserAdapter(
    private val bookmarkPrefs: BookmarkPrefs, private val onItemClick: (String) -> Unit
) : PagingDataAdapter<SearchUser, SearchUserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            UserSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, bookmarkPrefs, onItemClick) { position ->
            notifyItemChanged(position)
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    class UserViewHolder(
        private val binding: UserSearchItemBinding,
        private val bookmarkPrefs: BookmarkPrefs,
        private val onItemClick: (String) -> Unit,
        private val notifyUpdate: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchUser: SearchUser, position: Int) {
            val isBookmarked = bookmarkPrefs.isBookmarked(searchUser.login)

            binding.textViewUserName.text = searchUser.login
            ImageLoader.loadImage(searchUser.avatarUrl, binding.imageViewUser)

            binding.imageViewBookmark.setImageResource(
                if (isBookmarked) R.drawable.bookmark_24px_fill else R.drawable.bookmark_24px
            )

            binding.imageViewBookmark.setOnClickListener {
                if (isBookmarked) {
                    bookmarkPrefs.removeBookmark(searchUser.login)
                } else {
                    bookmarkPrefs.saveBookmark(searchUser.login, searchUser.avatarUrl)
                }
                notifyUpdate(position)
            }

            binding.root.setOnClickListener {
                onItemClick(searchUser.login)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<SearchUser>() {
        override fun areItemsTheSame(oldItem: SearchUser, newItem: SearchUser) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchUser, newItem: SearchUser) =
            oldItem == newItem
    }
}
