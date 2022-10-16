package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //crete variable of type navController then
        //get the navController from NavHostFragment and assign to this navController variable
        val navController:NavController = newsNavHostFragment.findNavController()
        //give the navController to the bottomNavigationView as to make bottom navigation control the navigation
        bottomNavigationView.setupWithNavController(navController)
    }
}
