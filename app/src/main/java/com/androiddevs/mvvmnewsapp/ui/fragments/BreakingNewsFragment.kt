package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

//instead of creating an empty fragment we can create a class and inherit the fragment and inside the bracket
//we put the payout of this fragment
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    val TAG="BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //this is to use the viewModel object created in the activity
        viewModel= (activity as NewsActivity).viewModel
        setupRecyclerView()
        loadData()
        onItemClick()
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility=View.VISIBLE
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
        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
        }
    }


    //setupRecyclerView
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }
    
    private fun loadData(){
        //when observing on liveData we create the mthod like this that we pass two parameter , the viewLifeCycleOwner and call back desribes as the function that
        //will be triggered if any change happens to this Observable that we observe to which is the LiveData this function is generated when we call pass Observer as new parameter which means creating new Observer
        //so having access to the callback of this Observer , it's the same as setOmClickListener, as we say x.setOnClickListener(Listener{}) in kotlin or x.setOnClickListener(new OnClickListener{}) in java
        //observe to the liveData
        //we can call this method like this way , which means that the parameter of type function  that is the callback function that will be executed whenever
        //summarizing : the parameter observer here is interface that will have the callback whenever the observable have any new data so the observer will get notified and call what is inside the callback of this interface
        //there is a change happens to tha liveData
        //        viewModel.breakingNews.observe(viewLifecycleOwner, {
        //
        //        })
        // or         viewModel.breakingNews.observe(viewLifecycleOwner) {
        //
        //        }
        //because the function parameter is the last parameter and we can move the curly brackets outside these brackets ()
        //or like this       viewModel.breakingNews.observe(viewLifecycleOwner, this) which is done after implementing the Observer inteerface and gives it the needed
        //type , it's like a callback provided by this interface
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {newsRespose->
                        newsAdapter.differ.submitList(newsRespose.articles)
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
}