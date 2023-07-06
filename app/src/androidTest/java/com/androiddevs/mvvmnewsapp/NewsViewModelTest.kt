package com.androiddevs.mvvmnewsapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import junit.framework.TestCase

/**
 * we created a class calledNewsViewModelTest to test the NewsViewModel class , and we annotated it with @runwith
 * (AndroidJUnit4) not Junit4 directly
 * as this is android component test , not normal tests for the logic that is put in test folders
 */
@RunWith(AndroidJUnit4::class)
class NewsViewModelTest :TestCase(){

    private lateinit var viewModelTest: NewsViewModelTest

    public override fun setUp() {
        super.setUp()
        val context=ApplicationProvider.getApplicationContext<android.content.Context>()

    }

}