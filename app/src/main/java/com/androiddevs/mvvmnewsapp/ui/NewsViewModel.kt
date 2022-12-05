package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.ui.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> =MutableLiveData()

    //using mutabeStateFlow instead of liveData to handle loading and error and success
    private val breakingNewsTwo = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())

    //will set the page number here and use pagination reference from here as to avoid the problems of configuration change to affect the logic
    var breakingNewsPage=1


    fun getBreakingNews(countryCode:String){
        //viewModelScope will make sure this coroutine methods stays a ive as along as the viewModel is alive
        viewModelScope.launch {
            //start the loading that represents the loading of network call
            breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        }
    }

    private fun handleNewsResponse(){

    }
}