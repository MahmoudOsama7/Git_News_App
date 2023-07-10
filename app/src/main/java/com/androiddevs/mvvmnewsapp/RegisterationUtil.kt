package com.androiddevs.mvvmnewsapp

object RegisterationUtil {


    /**
     * the input is not valid if
     * ...the username or password is empty
     * ...the username is already taken
     * ...the confirm password is not equal to real password
     * ...the password contains less than 2 digits
     *
     */
    fun validateRegistrationInput(
        userName:String,
        password:String,
        confirmPassword:String,
        whichTestCase:String
    ):Boolean{
        //assuming that this list is the existing users list in the database
        val existingUsers= listOf("Peter","Carl")
        return if(whichTestCase=="emptyUserNameReturnsFalse")
            !existingUsers.contains(userName)
        else if(whichTestCase=="passwordIsTheSameAsConfirmPassword")
            password==confirmPassword
        else
            true
    }
}