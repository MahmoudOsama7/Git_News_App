package com.androiddevs.mvvmnewsapp.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androiddevs.mvvmnewsapp.ui.Article


/**
 * this class annotates the sql database created by ROOM
 *
 *
 * we use annotation database to inform that this is our sql database here
 * we have parameters inside as entities which is the tables that will be inserted here we here only have one entity that is article
 *
 * then we need to specify version of database , this value here needs to be updated at each time we create an update in this database annotation
 * for ex , we have now one entity aricles so we can give value to version to be 1
 *
 * if we decided to add another entity , we need to update this version to be 2 for ex
 *
 *
 */

@Database(
    entities =[Article::class],
    version =1
)

//this class must inherit from ROOM database class as to get it's properties
abstract class ArticleDatabase:RoomDatabase() {
    //here we specify that the function articleDao is providing us with the interface to access the database that contains articles entity
    abstract fun getArticleDao():ArticlesDao

    //companion object is used to make anything singleton , the difference between it and object that companion object can be used inside class
    //but object is a type of class and we don't put it inside , we create it as single class like data class for ex
    //using volatile annotation as to make it memory safe that if tow threads access the database at same time they can both the changes directly , because
    //value changes directly in memory
    //also will create value of type Any() as to synchronize setting this room database instance as to make sure only since instance of room database at once
    companion object{
        @Volatile
        private var instance:ArticleDatabase?=null
        private val LOCK = Any()



        //one way of create singleton instance of Room database
        //to check first if the instance is null , so create synchronized block to create the needed database objedt
//        fun getInstance(context: Context):ArticleDatabase?{
//            if(instance==null){
//                synchronized(ArticleDatabase::class){
//                    instance= createDatabase(context)
//                }
//            }
//            return instance
//        }

        //other way of create singleton instance of Room database
        /**
         * create function of type operator and is called whenever we create instance of of roomDatabase
         * and will equalized the function with the instance of articleDatabaseCreated
         *
         * and in case the instance is null , we create synchronized function that takes the lock variable of type any we created
         * as to create new instance of room database , but thanks to synchronized , only one thread can enter to create this block to create the instance
         * and all other will access it after creation , which one can enter , the faster
         *
         * then inside will check again , if instance is null , create the database instance and give the value created to our variable instance
         *
         */
        operator fun invoke(context:Context)= instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{
                instance= it
            }
        }

        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "aritcle_db.db"
        ).build()
    }
}