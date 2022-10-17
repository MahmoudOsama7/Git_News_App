package com.androiddevs.mvvmnewsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androiddevs.mvvmnewsapp.ui.models.Article

/**
 *
 *
 * this interface used to access the sql database created by ROOM
 *
 * annotation dao used by room as interface from to access the database , like interface api in retrofit
 * we create insert annotation and inside it we give property like onConflict which means if what we want to insert in database is already in it
 * we choose here OnConflictStrategy.REPLACE meaning that if this conflict happens , replace the old value with the new value
 * the way of doing dao here is approximately the same as creating an interface for retrofit
 *
 *

 *
 *
 */

@Dao
interface ArticlesDao {

    /**
     *  * we create function upsert that will update the database with the article and gives back response which will here be value of type long of the id of this article
     * in the table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long


    /**
     * query by using sql language to select everything from articles table that we defined it using the entity annotation in articles data class
     *
     * will make this function normally not suspend as the response will be liveData and live data does not work with suspend function
     */

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>



    /**
     * delete function from database
     */

    @Delete
    suspend fun deleteArticle(article: Article)

}