package com.example.gitview.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitview.data.models.Repository
import com.example.gitview.databinding.UserRepoItemBinding

class UserRepoAdapter : PagingDataAdapter<Repository, UserRepoAdapter.RepoViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = UserRepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class RepoViewHolder(private val binding: UserRepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(repo: Repository) {
            binding.apply {
                textViewRepoName.text = repo.name
                textViewRepoDesc.text = repo.description ?: "No description"
                textStars.text = "${repo.stargazersCount}"
                textForks.text = "${repo.forksCount}"
            }
        }
    }
}
