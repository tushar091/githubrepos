package com.example.githubrepos.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.githubrepos.R
import com.example.githubrepos.constants.LIST_SIZE_ZERO
import com.example.githubrepos.constants.PULL_URL
import com.example.githubrepos.constants.RESPONSE_RECIEVED
import com.example.githubrepos.constants.SEARCH_CLICKED
import com.example.githubrepos.databinding.ActivityMainBinding
import com.example.githubrepos.model.PullRequestHolder
import com.example.githubrepos.ui.adapters.PullAdapter
import com.example.githubrepos.ui.viewModels.MainActivityViewModel
import com.example.githubrepos.utils.addOnScrolledToEnd

class MainActivity : BaseActivity(),PullAdapter.ClickListener {
    override fun onItemClicker(pull: PullRequestHolder) {
        viewModel.onItemClicked(pull)
    }

    lateinit var viewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var pullAdapter: PullAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        binding.model = viewModel
        recyclerView = binding.rvPullRequest
        pullAdapter = PullAdapter(this)
        with(recyclerView) {
            adapter = pullAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            val decor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decor)
        }
        registerObserver()
    }

    fun registerObserver() {
        viewModel.pullRequests.observe(this, Observer {
            pullAdapter.addDataList(it.orEmpty().toMutableList())
        })
        viewModel.loadRequest.observe(this, Observer {
            pullAdapter.addData(it)
            viewModel.fetchNextPage()
        })
        viewModel.details.observe(this, Observer {
            val detailIntent = Intent(this, PullDetailActivity::class.java)
            detailIntent.putExtra(PULL_URL,it)
            startActivity(detailIntent)
        })
        viewModel.uiAction.observe(this, Observer {
            when (it) {
                LIST_SIZE_ZERO -> {
                    pullAdapter.removeData()
                }
                SEARCH_CLICKED -> {
                    pullAdapter.setData(mutableListOf())
                }
                RESPONSE_RECIEVED->{
                    pullAdapter.removeData()
                    recyclerView.addOnScrolledToEnd{
                        viewModel.showLoader()
                        recyclerView.setOnScrollListener(null)
                    }
                }
            }
        })
    }


}