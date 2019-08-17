package com.example.githubrepos.ui.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.githubrepos.R
import com.example.githubrepos.databinding.ItemPullRequestBinding
import com.example.githubrepos.model.Pulls

class PullAdapter() : RecyclerView.Adapter<PullAdapter.PullViewHolder>() {
    var pullRequests: List<Pulls> = listOf()
    lateinit var binding: ItemPullRequestBinding

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PullViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_pull_request, parent, false)
        return PullViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pullRequests.size
    }

    override fun onBindViewHolder(holder: PullViewHolder, position: Int) {
        holder.bindDataToViewHolder(pullRequests[position], binding)
    }

    fun setData(data: List<Pulls>) {
        this.pullRequests = data
        notifyDataSetChanged()
    }

    class PullViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindDataToViewHolder(pullRequest: Pulls, binding: ItemPullRequestBinding) {
            binding.model = pullRequest
            binding.executePendingBindings()
        }

    }
}