package com.gulshansutey.mygithub.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gulshansutey.mygithub.R
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.ui.adapter.ReposRecyclerAdapter

class ReposViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.tv_repo_name)
    private val description: TextView = view.findViewById(R.id.tv_repo_description)
    private val stars: TextView = view.findViewById(R.id.tv_total_stars)
    private val language: TextView = view.findViewById(R.id.tv_repo_language)
    private val forks: TextView = view.findViewById(R.id.tv_total_forks)
    private val dotImage: ImageView = view.findViewById(R.id.iv_dot)

    private var gitRepo: GitRepo? = null

    init {
        view.setOnClickListener {
            if (view.context is ReposRecyclerAdapter.OnItemClickListener) {
                (view.context as ReposRecyclerAdapter.OnItemClickListener).onItemClick()
            }
        }
    }

    fun bind(gitRepo: GitRepo) {
        this.gitRepo = gitRepo
        name.text = gitRepo.fullName
        stars.text = gitRepo.stars.toString()
        forks.text = gitRepo.forks.toString()
        description.visibility = if (!gitRepo.description.isNullOrBlank()) {
            description.text = gitRepo.description
            View.VISIBLE
        } else {
            View.GONE
        }
        language.visibility = if (!gitRepo.language.isNullOrEmpty()) {
            language.text = gitRepo.language
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        fun create(parent: ViewGroup): ReposViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_repos_list, parent, false)
            return ReposViewHolder(view)
        }
    }

}
