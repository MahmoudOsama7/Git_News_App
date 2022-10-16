package com.androiddevs.mvvmnewsapp.ui.api

import com.androiddevs.mvvmnewsapp.ui.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {
    companion object{
        //we only initialize this variable only once
        //also lazy helps that it does not give memory to this variable until it's called which is a memory wise
        //option


        private val retrofit by lazy {
            //creating http logging interceptor as to see the retrofit post and get responses in the logcat
            //attach it to the level of the body of the response which is the part we care about here

            val logging=HttpLoggingInterceptor()
            logging
                .setLevel(HttpLoggingInterceptor.Level.BODY)


            //client of HTTP that we will add to the retrofit so that to see the response logs when doing
            //any request
            val client=OkHttpClient
                .Builder()
                .addInterceptor(logging)
                .build()

            //retrofit object builder that we will use to
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        }
        //the api itself that we will use the retrofit object to do the needed requests and then by the help
        //of interceptor will do see the logs of responses 
        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}