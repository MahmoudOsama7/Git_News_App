package com.androiddevs.mvvmnewsapp.ui

object Validator {
    fun validInput(searchQuery:String):Boolean{
        return searchQuery.isNotEmpty()
    }
}