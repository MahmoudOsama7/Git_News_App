package com.androiddevs.mvvmnewsapp.ui.util


/**
 * generic class to use to differentiate between successful and error responses when getting of posting data by using apis
 * will take variable of type T defining that any type class can use this generic class
 *
 * so the data type will be generic of type t to handle any type object and will be set nullable and = null by default
 * and message of type string also nullable and = null by default
 *
 *
 * sealed class : like abstract class but we can define which class exactly will be allowed to inherit from it
 * classes success , error and loading are inheriting from Resource sealed class
 */
sealed class Resource<T> (
    val data:T?=null,
    val message:String?=null
        ){
    //here the class will only contain value of type data so this case we check if the object of this class contain only value of data
    class Success <T>(data:T):Resource<T>(data)
    //here the class will contian value of type message and optional data so this case we check if the object of this class contain only value of message and optional data
    class Error <T>(message:String,data:T?=null):Resource<T>(data,message)
    //here the class will contain nothing so this case we check if the object of this class contain nothing
    class Loading<T>:Resource<T>()
}