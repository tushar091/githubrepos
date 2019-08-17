package com.example.githubrepos.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.githubrepos.R
import com.example.githubrepos.databinding.ActivityMainBinding
import com.example.githubrepos.ui.adapters.PullAdapter
import com.example.githubrepos.ui.viewModels.MainActivityViewModel

class MainActivity : BaseActivity() {
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
        pullAdapter = PullAdapter()
        with(recyclerView) {
            adapter = pullAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            val decor = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
            addItemDecoration(decor)
        }
        registerObserver()
    }

    fun registerObserver() {
        viewModel.pullRequests.observe(this, Observer {
            pullAdapter.setData(it.orEmpty())
        })
    }
}