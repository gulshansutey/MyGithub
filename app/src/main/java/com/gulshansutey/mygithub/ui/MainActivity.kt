package com.gulshansutey.mygithub.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    }

    private val adapter = ReposRecyclerAdapter()
    private lateinit var viewModel: MainActivityViewModel
    private var last_search_query: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        last_search_query = savedInstanceState?.getString(LAST_SEARCH_QUERY)
        viewModel = ViewModelProvider(this, Dependancies.provideViewModelFactory(this))
            .get(MainActivityViewModel::class.java)
        setSupportActionBar(toolbar)
        initUI()
        setupSortOptions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) return false

                viewModel.searchRepo(query)
                adapter.submitList(null)
                searchView.clearFocus()
                showEmptyScreen(false)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        if (!last_search_query.isNullOrBlank()) {
            searchView.setQuery(last_search_query, false)
        }
        return true
    }

    private fun setupSortOptions() {
        sort_spinner.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.sorting_options_array)
        )
        sort_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.filerData(sort_spinner.selectedItem as String)
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun initUI() {
        rv_search_results.layoutManager = LinearLayoutManager(this)
        rv_search_results.adapter = adapter

        viewModel.gitRepos.observe(this, Observer<List<GitRepo>> {
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

        adapter.setItemClickListener(object : ReposRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(gitRepo: GitRepo?) {
                if (gitRepo != null) {
                    val detailActivityIntent =
                        Intent(this@MainActivity, RepoDetailActivity::class.java)
                    detailActivityIntent.putExtra(RepoDetailActivity.EXTRA_DATA, gitRepo)
                    startActivity(detailActivityIntent)
                }
            }

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
            viewModel.searchRepo(viewModel.lastQueryValue()!!)
            adapter.submitList(null)
            dialogInterface.dismiss()
        }
        alertDialogBuilder.create().show()
    }
}
