package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.androiddevs.mvvmnewsapp.ui.models.NewsResponse
import com.androiddevs.mvvmnewsapp.ui.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.ui.util.Resource
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> =MutableLiveData()
    //will set the page number here and use pagination reference from here as to avoid the problems of configuration change to affect the logic
    var breakingNewsPage=1
    //using mutabeStateFlow instead of liveData to handle loading and error and success
//    private val breakingNewsTwo = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())

    val searchNews:MutableLiveData<Resource<NewsResponse>> =MutableLiveData()
    var searchNewsPage=1


    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode:String){
        //viewModelScope will make sure this coroutine methods stays a ive as along as the viewModel is alive
        viewModelScope.launch {
            //start the loading that represents the loading of network call so when the method is called a loading is happening
            breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
            //here the breakingNewsMutableLiveData will have the value of either Resource.Success or Resource.Error and inside this Resource will have either
            //the data or the error message and based on that type will give the postValue variable of type mutableLiveData the needed value
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }

    //check to apply core way to check network response is success , error or loading
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
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

    fun getSearchNews(searchQuery:String){
        //viewModelScope will make sure this coroutine methods stays a ive as along as the viewModel is alive
        viewModelScope.launch {
            //start the loading that represents the loading of network call so when the method is called a loading is happening
            searchNews.postValue(Resource.Loading())
            val response = newsRepository.searchNews(searchQuery,searchNewsPage)
            //here the breakingNewsMutableLiveData will have the value of either Resource.Success or Resource.Error and inside this Resource will have either
            //the data or the error message and based on that type will give the postValue variable of type mutableLiveData the needed value
            searchNews.postValue(handleSearchNewsResponse(response))
        }
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
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

    //we use a coroutine scope which is viewModel scope as the function we will call inside is a suspend function
    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }
    //function to get savedNews , normal function as using live data , TODO will change to flow
    fun getSavedNews()=newsRepository.getSavedNews()

    //suspend function to delete article
    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}