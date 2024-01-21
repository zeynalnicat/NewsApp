package com.example.newsapp.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.databinding.ItemSavedNewsBinding
import com.example.newsapp.db.BookmarkEntity
import com.example.newsapp.model.Article
import com.example.newsapp.model.Source

class BookMarkAdapter(private val nav: (Bundle) -> Unit) :
    RecyclerView.Adapter<BookMarkAdapter.ViewHolder>() {
    private val diffCall = object : DiffUtil.ItemCallback<BookmarkEntity>() {
        override fun areItemsTheSame(oldItem: BookmarkEntity, newItem: BookmarkEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: BookmarkEntity,
            newItem: BookmarkEntity,
        ): Boolean {
            return oldItem == newItem
        }
    }


    private val diffUtil = AsyncListDiffer(this, diffCall)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSavedNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(diffUtil.currentList[position])
    }


    inner class ViewHolder(private val binding: ItemSavedNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: BookmarkEntity) {
            Glide.with(binding.root)
                .load(article.imgUrl)
                .placeholder(com.example.newsapp.R.drawable.noimage)
                .into(binding.imgArticle)

            binding.txtNewsTitle.text = article.title
            binding.txtDate.text = article.date
            binding.txtCategory.text = article.source
            binding.txtAuthorName.text = article.author

            itemView.setOnClickListener {
                val articleModel = Article(
                    article.author, article.content, article.description, article.date,
                    Source("", article.source), article.title, article.url, article.imgUrl
                )
                val bundle = Bundle()
                bundle.putSerializable("article", articleModel)
                nav(bundle)
            }
        }
    }

    fun submitList(articles: List<BookmarkEntity>) {
        diffUtil.submitList(articles)
    }

}