package com.gulshansutey.mygithub.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gulshansutey.mygithub.Dependancies
import com.gulshansutey.mygithub.R
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.model.RepoContributor
import com.gulshansutey.mygithub.ui.adapter.ContributorsRecyclerAdapter
import com.gulshansutey.mygithub.viewmodel.RepoDetailActivityViewModel
import kotlinx.android.synthetic.main.activity_repo_detail.*
import kotlinx.android.synthetic.main.repo_detail_contributors_layout.*

class RepoDetailActivity : AppCompatActivity() {

    companion object {
        var EXTRA_DATA = "extra_repo_details"
    }

    private val adapter = ContributorsRecyclerAdapter()
    private lateinit var viewModel: RepoDetailActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, Dependancies.provideViewModelFactory(this)).get(
            RepoDetailActivityViewModel::class.java
        )
        setContentView(R.layout.activity_repo_detail)
        initUI()
    }

    private fun initUI() {
        val gitRepo = intent.getParcelableExtra(EXTRA_DATA) as GitRepo
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar_layout.title = gitRepo.name
        Glide.with(this).load(gitRepo.owner.avatar_url).into(toolbar_image)
        rv_contributors_list.layoutManager = LinearLayoutManager(this)
        rv_contributors_list.adapter = adapter
        viewModel.repoContributor.observe(this, Observer<List<RepoContributor>> {
            adapter.submitList(it)
        })

        viewModel.isRequestInProgress.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

        })
        viewModel.getContributors(gitRepo.contributorsUrl)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}