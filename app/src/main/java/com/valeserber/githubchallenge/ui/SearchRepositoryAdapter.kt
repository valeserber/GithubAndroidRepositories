package com.valeserber.githubchallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.valeserber.githubchallenge.databinding.ItemGithubSearchBinding
import com.valeserber.githubchallenge.domain.Repository

class SearchRepositoryAdapter(val clickListener: SearchRepositoryListener) :
    PagedListAdapter<Repository, SearchRepositoryAdapter.ViewHolder>(SearchRepositoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(clickListener, item)
        }
        //when item is null we can implement a placeholder
    }


    class ViewHolder private constructor(val binding: ItemGithubSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: SearchRepositoryListener, item: Repository) {
            binding.repository = item
            binding.clickListener = listener
            binding.nameText.text = item.name
            binding.urlText.text = item.url
            binding.criteriaText.text = "Stars" //TODO change
            binding.criteriaValueText.text = item.starsCount.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGithubSearchBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SearchRepositoryListener(val clickListener: (repositoryId: Long) -> Unit) {
    fun onClick(repository: Repository) = clickListener(repository.id)
}

class SearchRepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {

    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }
}