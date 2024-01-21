package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentBookmarkBinding
import com.example.newsapp.db.BookmarkEntity
import com.example.newsapp.db.RoomDb
import com.example.newsapp.resource.Resource
import com.example.newsapp.ui.adapters.BookMarkAdapter
import com.example.newsapp.viewmodel.BookmarkViewModel
import com.example.newsapp.viewmodel.factory.BookmarkFactory

class BookmarkFragment : Fragment() {
    private lateinit var binding:FragmentBookmarkBinding
    private lateinit var roomDb: RoomDb
    private val bookmarkViewModel : BookmarkViewModel by viewModels { BookmarkFactory(roomDb) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomDb = RoomDb.accessDb(requireContext())!!
        bookmarkViewModel.bookmarks.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success->{
                    setAdapter(it.data)
                }
                is Resource.Error ->{
                    Toast.makeText(requireContext(),it.exception.message!!,Toast.LENGTH_SHORT).show()
                }
                else ->{

                }
            }

        }

        bookmarkViewModel.getBookmarks()
    }

    private fun setAdapter(articles:List<BookmarkEntity>){
        val adapter = BookMarkAdapter{findNavController().navigate(R.id.action_bookmarkFragment_to_singleArticleFragment,it)}
        adapter.submitList(articles)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),1)
        binding.recyclerView.adapter = adapter
    }


}