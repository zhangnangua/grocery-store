package com.pumpkin.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pumpkin.mvvm.databinding.FooterLayoutBinding

class FooterAdapter(val retry: (() -> Unit)? = null) :
    LoadStateAdapter<FooterAdapter.ViewHolder>() {
    class ViewHolder(val binding: FooterLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        val binding = holder.binding
        when (loadState) {
            is LoadState.Error -> {
                binding.progress.visibility = View.GONE
//                binding.retryButton.text = "Load Failed, Tap Retry"
//                binding.retryButton.setOnClickListener {
//                    retry()
//                }
            }
            is LoadState.Loading -> {
                binding.progress.visibility = View.VISIBLE
//                binding.retryButton.visibility = View.VISIBLE
//                binding.retryButton.text = "Loading"
            }
            is LoadState.NotLoading -> {
                binding.progress.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            FooterLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}