package com.androiddevs.mvvmnewsapp

import com.google.common.truth.Truth

import org.junit.Test


class RegisterationUtilTest{
    @Test
    fun emptyUserNameReturnsFalse(){
        val result = RegisterationUtil.validateRegistrationInput(
            userName = "",
            password = "123",
            confirmPassword = "123",
            whichTestCase = "emptyUserNameReturnsFalse"
        )
        Truth.assertThat(result).isTrue()
    }
}

//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    private val resourceComparer=ResourceComparer()
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.androiddevs.shoppinglisttestingyt", appContext.packageName)
//    }
//}