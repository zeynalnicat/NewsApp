package com.example.newsapp.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSingleArticleBinding
import com.example.newsapp.db.BookmarkEntity
import com.example.newsapp.db.RoomDb
import com.example.newsapp.model.Article
import com.example.newsapp.resource.Resource
import com.example.newsapp.viewmodel.SingleArticleViewModel
import com.example.newsapp.viewmodel.factory.SingleArticleFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale


class SingleArticleFragment : Fragment() {
    private lateinit var binding: FragmentSingleArticleBinding
    private lateinit var roomDb: RoomDb
    private val singleArticleViewModel: SingleArticleViewModel by viewModels {
        SingleArticleFactory(
            roomDb
        )
    }
    private var article: Article? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleArticleBinding.inflate(inflater)
        setNavigation()
        setLayout()
        return binding.root
    }

    private fun setNavigation() {
        binding.viewBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomDb = RoomDb.accessDb(requireContext())!!
        singleArticleViewModel.insertion.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data.toInt() == -1) {
                        Toast.makeText(requireContext(), "Removed from bookmark", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Added to bookmark", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                is Resource.Error -> {
                    Snackbar.make(requireView(), it.exception.message!!, Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {

                }
            }
        }

        singleArticleViewModel.check.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.iconAddBookmark.setImageResource(if (it.data == 0) R.drawable.bookmark_blue else R.drawable.bookmark_blue_filled)
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.exception.message!!, Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}

            }
        }

        binding.iconAddBookmark.setOnClickListener {
            article?.let { article ->
                val bookmark = BookmarkEntity(
                    source = article.source.name ?: "",
                    imgUrl = article.urlToImage ?: "",
                    author = article.author ?: "",
                    title = article.title ?: "",
                    date = article.publishedAt ?: "",
                    description = article.description ?: "",
                    content = article.content?:"",
                    url = article.url?:""
                )
                singleArticleViewModel.insert(bookmark)

            }

        }

        article?.let {
            singleArticleViewModel.checkDb(it.title!!)
        }


    }


    private fun setLayout() {
        arguments?.let {
            article = it.getSerializable("article") as Article

            article?.let { article ->
                Glide.with(binding.root)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.noimage)
                    .into(binding.imgArticle)

                binding.txtAuthorName.text =
                    if (article.author.isNullOrBlank()) "Anonym" else article.author
                binding.txtHeader.text = article.title
                binding.txtCategory.text = article.source.name
                binding.txtNewtsDate.text = article.publishedAt
                binding.txtNewsDescription.text = article.description
                binding.txtContent.text = article.content

                binding.txtReadMore.autoLinkMask = Linkify.WEB_URLS

                binding.txtReadMore.setOnClickListener {
                    val url = article.url
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
            }
        }


    }


}