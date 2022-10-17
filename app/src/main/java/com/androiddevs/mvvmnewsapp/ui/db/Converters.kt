package com.androiddevs.mvvmnewsapp.ui.db

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.ui.models.Source

class Converters {

    //converting from Source class type to string , as to be used by ROOM database
    //annotation type converters as to inform room that this is the type converter method to use to convert form source to string to store in database
    //here we care about only source name to convert , but if we want to convert full source class , we will create gson object to convert to and from
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    //type converter method to convert from string to Source again
    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}