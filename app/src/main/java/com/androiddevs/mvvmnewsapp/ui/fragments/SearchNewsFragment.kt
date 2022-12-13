package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
        onItemClick()

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
    private fun onItemClick(){
        //as already in the newsAdapter , we have             setOnClickListener {
        //                onItemClickListener?.let {
        //                    it(article) }
        //            }  when clicking on any item in the recyclerView , so when clicking , the onItemClickListener is variable of type lambda function
        //that takes the article thanks to this line of code it(article) that means it "the method" takes the article parameter so now when clicking on any itemview
        //article is passed to the lambda function
        //then here calling newsAdapter.setOnItemClickListener
        //newsAdapter.setOnItemClickListener is a function and since last parameter is a lambda function so can use it like this setOnClickListener({}) or
        //keep the curly brackets outside , so here the curly brackets outside
        // so what inside the curly brackets is the parameter passed to this function , when we say it we say that it will be passed from the function setOnClickListener
        //itself , not from here
        searchNewsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,bundle)
        }
    }

}