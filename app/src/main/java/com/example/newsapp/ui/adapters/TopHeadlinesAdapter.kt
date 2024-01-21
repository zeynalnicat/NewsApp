package com.example.newsapp.ui.adapters

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.livedata.core.ktx.R
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article

class TopHeadlinesAdapter(private val nav : (Bundle)->Unit) : RecyclerView.Adapter<TopHeadlinesAdapter.ViewHolder>() {

    private val diffCall = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }


    private val diffUtil = AsyncListDiffer(this, diffCall)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(diffUtil.currentList[position])
    }


    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            Glide.with(binding.root)
                .load(article.urlToImage)
                .placeholder(com.example.newsapp.R.drawable.noimage)
                .into(binding.imgNews)

            binding.txtNewsTitle.text = article.title

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("article",article)
                nav(bundle)
            }
        }
    }

    fun submitList(articles: List<Article>) {
        diffUtil.submitList(articles)
    }

}