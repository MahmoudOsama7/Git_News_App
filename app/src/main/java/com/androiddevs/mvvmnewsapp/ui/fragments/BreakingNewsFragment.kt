package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//instead of creating an empty fragment we can create a class and inherit the fragment and inside the bracket
//we put the payout of this fragment
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    var isLoading=false
    var isLastPage=false
    var isScrolling=false
    var scrollListener:RecyclerView.OnScrollListener?=null


    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    val TAG="BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //this is to use the viewModel object created in the activity
        viewModel= (activity as NewsActivity).viewModel
        paginate()
        setupRecyclerView()
        loadData()
        onItemClick()
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
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

    private fun paginate(){
        scrollListener =object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //this if check means that we are currently scrolling
                if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true
                }
            }
            //to get the state when scrolled we need to do some calculations
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager=recyclerView.layoutManager as LinearLayoutManager
                //getting first visible item by getting first visible position in recyclerView
                val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
                //getting visible count meaning number of items in list we can see when scrolling
                val visibleItemCount=layoutManager.childCount
                //getting total item count which mean total amount of items in the list
                val totalItemCount=layoutManager.itemCount
                //checking if we are not loading and not in the last page
                //meaning that we are at list and scrolling but did not reach the last page and did not load
                //any new page list
                val isNotLoadingAndNotLastPage=!isLoading && !isLastPage
                //getting that we are at the last item , which to know we need to get first visible item position
                //and add to it the total item visible count that we can see at the moment we are scrolling
                //if more than or equal total item count in the list so we reached last element
                //firstVisibleItemposisiton is not constant , it's increasing because we are scrolling in the list
                val isAtLastItem=firstVisibleItemPosition+visibleItemCount >=totalItemCount
                //checking is not at begining by checking value of firstVisibleItemPosition
                val isNotAtBegining=firstVisibleItemPosition>=0
                //checking if total is more than visible by checking if totalItemCount of the page is > or =
                //size of page
                val isTotalMoreThanVisible=totalItemCount>=QUERY_PAGE_SIZE
                val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBegining
                        && isTotalMoreThanVisible && isScrolling

                if(shouldPaginate){
                    viewModel.getBreakingNews("us")
                    isScrolling=false

                }
            }
        }
    }


    //setupRecyclerView
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener!!)
        }
    }
    
    private fun loadData() {
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
//        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
//            when (response) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    response.data?.let { newsRespose ->
//                        newsAdapter.differ.submitList(newsRespose.articles)
//                        //totalResults is a parameter in the class newsResponse that define the total size of the pages retrieved in total
//                        //we divide it by QUERY_PAGE_SIZE to know how many pages we have to load
//                        //why +2  , as the division result always rounded so not getting full pages so we add 1 to get full pages ,
//                        // also the last page is empty respose
//                        //so we have to add another 1 to avoid this
//                        val totalPages = newsRespose.totalResults / QUERY_PAGE_SIZE + 2
//                        //if we are at the last page so viewModel.breakingNewsPage==totalPages will give result as true
//                        //so if false , we are not at the last page as did not reach total pages
//                        isLastPage = viewModel.breakingNewsPage == totalPages
//
//                        if (isLastPage) {
//                            rvBreakingNews.setPadding(0, 0, 0, 0)
//                        }
//                    }
//                }
//                is Resource.Error -> {
//                    hideProgressBar()
//                    response.data?.let { message ->
//                        Log.e(TAG, "An error occured $message")
//                    }
//                }
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//            }
//        })
        lifecycleScope.launch {
            viewModel.breakingNewsTwo.collectLatest { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { newsRespose ->
                            newsAdapter.differ.submitList(newsRespose.articles)
                            //totalResults is a parameter in the class newsResponse that define the total size of the pages retrieved in total
                            //we divide it by QUERY_PAGE_SIZE to know how many pages we have to load
                            //why +2  , as the division result always rounded so not getting full pages so we add 1 to get full pages ,
                            // also the last page is empty respose
                            //so we have to add another 1 to avoid this
                            val totalPages = newsRespose.totalResults / QUERY_PAGE_SIZE + 2
                            //if we are at the last page so viewModel.breakingNewsPage==totalPages will give result as true
                            //so if false , we are not at the last page as did not reach total pages
                            isLastPage = viewModel.breakingNewsPage == totalPages

                            if (isLastPage) {
                                rvBreakingNews.setPadding(0, 0, 0, 0)
                            }
                        }
                    }
                    is Resource.Loading ->{
                        showProgressBar()
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        response.data?.let { message ->
                            Log.e(TAG, "An error occured $message")
                        }
                    }
                }
            }
        }
    }
}