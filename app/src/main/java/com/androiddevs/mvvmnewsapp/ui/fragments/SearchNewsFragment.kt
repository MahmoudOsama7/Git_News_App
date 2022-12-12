package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var searchNewsAdapter: NewsAdapter
    private val TAG="SearchgNewsFragment"
    var job:Job?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= (activity as NewsActivity).viewModel
        setupRecyclerView()
        setupSearch()
        loadData()


    }
    private fun hideProgressBar() {
        rvSearchNews.visibility=View.VISIBLE
        paginationProgressBar.visibility=View.INVISIBLE
    }

    private fun showProgressBar() {
        rvSearchNews.visibility=View.INVISIBLE
        paginationProgressBar.visibility=View.VISIBLE
    }

    //setupRecyclerView
    private fun setupRecyclerView(){
        searchNewsAdapter= NewsAdapter()
        rvSearchNews.apply {
            adapter=searchNewsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
    private fun loadData(){
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {newsRespose->
                        searchNewsAdapter.differ.submitList(newsRespose.articles)
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.data?.let { message->
                        Log.e(TAG,"An error occured $message")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }
    private fun setupSearch(){
        etSearch.addTextChangedListener {editable->
            job?.cancel()
            job= lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }
    }
}