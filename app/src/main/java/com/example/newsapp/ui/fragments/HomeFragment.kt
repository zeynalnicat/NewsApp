package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.model.Article
import com.example.newsapp.resource.Resource
import com.example.newsapp.ui.adapters.TopHeadlinesAdapter
import com.example.newsapp.viewmodel.HomeViewModel
import com.example.newsapp.R
import com.example.newsapp.db.BookmarkEntity
import com.example.newsapp.ui.adapters.BookMarkAdapter
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        val date = java.util.Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        binding.txtDate.text = formattedDate
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search()
        homeViewModel.topHeadlines.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    setAdapter(it.data)
                    binding.progressBar.visibility = View.GONE
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }

                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        homeViewModel.searchedNews.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val searchedArticles = it.data.map {
                        BookmarkEntity(

                            id = 0,
                            source = it.source.name ?: "",
                            title = it.title ?: "",
                            date = it.publishedAt ?: "",
                            author = it.author ?: "",
                            imgUrl = it.urlToImage ?: "",
                            description = it.description ?: "",
                            content = it.content ?: "",
                            url = it.url ?: ""

                        )
                    }
                    searchAdapter(searchedArticles)
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                }
            }
        }

        homeViewModel.getTopHeadlines()

    }


    private fun setAdapter(articles: List<Article>) {
        val adapter = TopHeadlinesAdapter {
            findNavController().navigate(
                R.id.action_homeFragment_to_singleArticleFragment,
                it
            )
        }
        adapter.submitList(articles)
        binding.recyclerTopHeadlines.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerTopHeadlines.adapter = adapter
    }


    private fun search() {
        binding.edtSearch.doAfterTextChanged {
            if(it.isNullOrEmpty()){
                binding.recyclerViewSearch.visibility = View.GONE
            }else{
                binding.recyclerViewSearch.visibility= View.VISIBLE
                homeViewModel.search(it.toString())
            }

        }
    }

    private fun searchAdapter(news: List<BookmarkEntity>) {
        val adapter =
            BookMarkAdapter { findNavController().navigate(R.id.action_homeFragment_to_singleArticleFragment,it) }
        adapter.submitList(news)
        binding.recyclerViewSearch.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewSearch.adapter = adapter
    }


}