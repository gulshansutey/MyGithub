package com.gulshansutey.mygithub.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.ui.adapter.viewholder.ReposViewHolder

class ReposRecyclerAdapter : ListAdapter<GitRepo, RecyclerView.ViewHolder>(LIST_ITEM_COMPARATOR) {


    lateinit var onItemClickListener: OnItemClickListener

    companion object {
        private val LIST_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<GitRepo>() {
            override fun areItemsTheSame(oldItem: GitRepo, newItem: GitRepo): Boolean =
                oldItem.fullName == newItem.fullName

            override fun areContentsTheSame(oldItem: GitRepo, newItem: GitRepo): Boolean =
                oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReposViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as ReposViewHolder).bind(repoItem)
        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(gitRepo: GitRepo?)
    }
}