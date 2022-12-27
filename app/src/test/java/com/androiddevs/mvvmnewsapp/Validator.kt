package com.androiddevs.mvvmnewsapp

object Validator {
    fun validInput(searchQuery:String):Boolean{
        return searchQuery.isNotEmpty()
    }
}