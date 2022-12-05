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
import retrofit2.Response

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
            //here the breakingNewsMutableLiveData will have the value of either Resource.Success or Resource.Error and inside this Resource will have either
            //the data or the error message
            breakingNews.postValue(handleNewsResponse(response))
        }
    }

    private fun handleNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        //checking if response is success , will return Resource.Success and the response itself
        if(response.isSuccessful){
            //after checking response is successful , will check it's not null and will use let that only perform the inside block of code if response not null
            response.body()?.let {  resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        //checking if response is error , will return Resource.Error and the error message
        return Resource.Error(response.message())
    }
}