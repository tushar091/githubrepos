package com.example.githubrepos.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.example.githubrepos.R
import com.example.githubrepos.databinding.ActivityMainBinding
import com.example.githubrepos.ui.viewModels.MainActivityViewModel

class MainActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    var viewModel: MainActivityViewModel? = null
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        binding?.model = viewModel
    }
}