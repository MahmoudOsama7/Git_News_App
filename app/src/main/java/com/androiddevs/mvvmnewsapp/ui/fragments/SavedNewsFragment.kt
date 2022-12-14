package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.adapters.NewsAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*

class SavedNewsFragment:Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var savedNewsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        onItemClick()
        getSavedArticles()
        addTouchToDeleteOption(view)
    }

    private fun onItemClick() {
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
        // for the lambda function , we have it like this {argument->body} which mean that the argument is the argument
        //passed and then comes the body which is the code of the lambda function or in other word what will this lambda function do
        savedNewsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle)
        }
    }

    //setupRecyclerView
    private fun setupRecyclerView() {
        savedNewsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = savedNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getSavedArticles(){
        Log.d("3ash", "1: ")
        viewModel.getSavedNews().observe(viewLifecycleOwner){ articles->
            savedNewsAdapter.differ.submitList(articles)
        }
    }

    private fun addTouchToDeleteOption(view:View){
        val itemTouchHelperCallback=object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //get the position of the item we swiped
                val position=viewHolder.adapterPosition
                //get the article of the itemView of the recyclerView that we swiped  by getting current list from differ object and then pass the
                //current position
                val article=savedNewsAdapter.differ.currentList[position]
                //then calling the delete function inside the viewModel to delete it
                viewModel.deleteArticle(article)

                //adding unDo function as if we swiped by mistake
                Snackbar.make(view,"Successfully deleted article",Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }.show()
                }
            }
        }
        //then calling the callback itself to inform it that the recyclerView that we will do the option of swipe to delete will be rvSavedNews recyclerView
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }
}