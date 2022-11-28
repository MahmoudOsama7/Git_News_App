package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.ui.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {


    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //creating repository class manually
        val newsRepository=NewsRepository(ArticleDatabase(this))

        //creating viewModelFactory as to pass the viewModel with constructor that contain arguments
        val viewModelProviderFactory=NewsViewModelProviderFactory(newsRepository)
        //initializing the viewModel object and giving it the viewModelFactory we created
        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        //crete variable of type navController then
        //get the navController from NavHostFragment and assign to this navController variable
        val navController:NavController = newsNavHostFragment.findNavController()
        //give the navController to the bottomNavigationView as to make bottom navigation control the navigation
        bottomNavigationView.setupWithNavController(navController)
    }
}
