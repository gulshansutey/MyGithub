package com.gulshansutey.mygithub.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gulshansutey.mygithub.R
import com.gulshansutey.mygithub.model.RepoContributor

class ContributorListViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.tv_name)
    private val avatar: ImageView = view.findViewById(R.id.iv_avatar)
    private var repoContributor: RepoContributor? = null


    fun bind(repoContributor: RepoContributor) {
        this.repoContributor = repoContributor
        name.text = repoContributor.name
        Glide.with(itemView.context).load(repoContributor.avatar_url).into(avatar)
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): ContributorListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_contributor_list, parent, false)
            return ContributorListViewHolder(view)
        }
    }
}