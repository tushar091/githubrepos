package com.example.githubrepos.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.githubrepos.R
import com.example.githubrepos.constants.PULL_URL
import com.example.githubrepos.databinding.ActivityPullDetailsBinding
import com.example.githubrepos.ui.viewModels.DetailActivityViewModel

class PullDetailActivity : BaseActivity() {
    lateinit var viewModel: DetailActivityViewModel
    lateinit var dataBinding: ActivityPullDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_pull_details)
        viewModel = ViewModelProviders.of(this)[DetailActivityViewModel::class.java]
        if (intent != null) {
            val url = intent.getStringExtra(PULL_URL)
            viewModel.url = url
        }
        dataBinding.model = viewModel
        viewModel.fetchDetails()
    }
}