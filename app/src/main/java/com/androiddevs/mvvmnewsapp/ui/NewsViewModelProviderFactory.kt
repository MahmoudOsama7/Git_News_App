package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.ui.repository.NewsRepository


/**
 * viewModelFactory is the same as property delegation but delegation is way more easier and cleaner
 * however if we are using a constructor with parameters we need to create a factory to handle this or we can use dependency injection so that the paramters
 * are injected by default , they are not passed from outside it's like no arguments and at that case no need for factory
 *
 *
 *
 */
class NewsViewModelProviderFactory(
    val newsRepository: NewsRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}