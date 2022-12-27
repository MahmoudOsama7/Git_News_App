package com.androiddevs.mvvmnewsapp


import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest{


    /**
     * what happens here is that
     * we have a value of query as searchQuery that must not be empty , i give here a value of us
     * then called the validInputMethod and pass to it the searchQuery , this validate method will return true if not empty and false if empty
     * then called assertThat method in google truth library that
     * will take the result and assert that it's equal to true , if true meaning that searchQuery not empty and test succeeded , if false
     * meaning that searchQuery value is empty and test failed
     *
     * if i entered searchQuery with value , so test will succeed and if no value , test will fail
     *
     * ex failed : output ->
     * expected: true
     * but was : false
     * Expected :true
     * Actual   :false
     *
     * ex succeed : output ->
     * Process finished with exit code 0
     *
     *
     * so in general in test , we put all test cases here and check for it if valid or not by using functions here
     * as here for ex we gave a test case that searchQuery must contain value and not empty string
     * to be valid
     */
    @Test
    fun whenInputIsValid(){
        val searchQuery="us"
        val result= Validator.validInput(searchQuery)
        assertThat(result).isEqualTo(true)
    }
}