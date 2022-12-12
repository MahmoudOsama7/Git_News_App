package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.ui.models.Article
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment:Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    //this type ArticleFragmentArgs is generated after adding the custom serializable in the navigation then build as we added
    //article as parameter custom serializable to be transferred in the navigation
    //we used property delegation as to inform that our variable will have characteristics of navArgs(
    val args:ArticleFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= (activity as NewsActivity).viewModel
        //giving the method the article attribute received from the navigation
        setWebView(args.article)
    }

    private fun setWebView(article:Article){
        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }
    }
}