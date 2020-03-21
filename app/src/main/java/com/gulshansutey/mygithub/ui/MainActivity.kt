package com.gulshansutey.mygithub.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gulshansutey.mygithub.Dependancies
import com.gulshansutey.mygithub.R
import com.gulshansutey.mygithub.model.GitRepo
import com.gulshansutey.mygithub.ui.adapter.ReposRecyclerAdapter
import com.gulshansutey.mygithub.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Kotlin"
    }

    private val adapter = ReposRecyclerAdapter()
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, Dependancies.provideViewModelFactory(this))
            .get(MainActivityViewModel::class.java)
        rv_search_results.layoutManager = LinearLayoutManager(this)

        setupSearchBar()
        initAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun setupSearchBar() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query==null)return false

                viewModel.searchRepo(query.toString())
                adapter.submitList(null)
                search_view.clearFocus()
                showEmptyScreen(false)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    private fun initAdapter() {
        rv_search_results.adapter = adapter

        viewModel.gitRepos.observe(this, Observer<List<GitRepo>> {
            Log.d("Activity", "list: ${it?.size}")
            showEmptyScreen(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.isRequestInProgress.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

        })
        viewModel.networkErrors.observe(this, Observer<String> {

            showRetryAlert(it)

        })
    }

    private fun showEmptyScreen(show: Boolean) {
        if (show) {
            view_empty_screen.visibility = View.VISIBLE
            rv_search_results.visibility = View.GONE
        } else {
            view_empty_screen.visibility = View.GONE
            rv_search_results.visibility = View.VISIBLE
        }
    }

    private fun showRetryAlert(message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(
            "Retry"
        ) { dialogInterface, _ ->
            viewModel.searchRepo(viewModel.lastQueryValue()?: DEFAULT_QUERY)
            adapter.submitList(null)
            dialogInterface.dismiss()
        }
        alertDialogBuilder.create().show()
    }
}
