package com.example.githubrepos.ui.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.githubrepos.R
import com.example.githubrepos.databinding.ItemLoadingBinding
import com.example.githubrepos.databinding.ItemPullRequestBinding
import com.example.githubrepos.model.PullRequestHolder
import com.example.githubrepos.model.TYPE_PULLS

class PullAdapter(val listener: ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var pullRequests = mutableListOf<PullRequestHolder>()
    lateinit var binding: ItemPullRequestBinding
    lateinit var loadBinding: ItemLoadingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PULLS -> {
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_pull_request, parent, false)
                PullViewHolder(binding)
            }
            else -> {
                loadBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.item_loading, parent, false)
                LoaderViewHolder(loadBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return pullRequests[position].type
    }

    override fun getItemCount(): Int {
        return pullRequests.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (pullRequests[position].type) {
            TYPE_PULLS -> (holder as PullViewHolder).bindDataToViewHolder(pullRequests[position], listener)
            else -> {
                (holder as LoaderViewHolder).bindDataToViewHolder()
            }
        }
    }

    fun setData(data: MutableList<PullRequestHolder>) {
        this.pullRequests = data
        notifyDataSetChanged()
    }

    fun addData(data: PullRequestHolder?) {
        if (data == null) {
            return
        }
        val pos = pullRequests.size - 1
        this.pullRequests.add(data)
        notifyItemInserted(pos + 1)
    }

    fun removeData() {
        val pos = pullRequests.size - 1
        if (pos < 0) {
            return
        }
        this.pullRequests.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun addDataList(data: List<PullRequestHolder>) {
        val initial = pullRequests.size
        pullRequests.addAll(data)
        notifyItemRangeInserted(initial, initial + data.size)
    }

    class PullViewHolder(val binding: ItemPullRequestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindDataToViewHolder(pullRequest: PullRequestHolder, listener: ClickListener) {
            binding.model = pullRequest
            binding.executePendingBindings()
            binding.root.setOnClickListener { listener.onItemClicker(pullRequest) }
        }

    }

    class LoaderViewHolder(val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindDataToViewHolder() {
            binding.executePendingBindings()
        }
    }

    interface ClickListener {
        fun onItemClicker(pull: PullRequestHolder)
    }
}