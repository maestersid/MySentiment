package com.xamarin.azuredevdays.uitest

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View

import com.jakewharton.espresso.OkHttp3IdlingResource
import com.xamarin.azuredevdays.HTTP_CLIENT
import com.xamarin.azuredevdays.MainActivity
import com.xamarin.azuredevdays.R
import com.xamarin.testcloud.espresso.Factory
import com.xamarin.testcloud.espresso.ReportHelper

import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class EmojiTestRecord {

    @Rule @JvmField val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule @JvmField val reportHelper: ReportHelper = Factory.getReportHelper()

    @Test
    fun emojiTestRecord() {
        val sentimentText = onView(
            allOf<View>(
                ViewMatchers.withId(R.id.sentimentText),
                withParent(withId(R.id.backgroundLayout)),
                isDisplayed()
            )
        )

        reportHelper.label("On Main Page")
        sentimentText.perform(click())
        sentimentText.perform(replaceText("Happy"), closeSoftKeyboard())
        reportHelper.label("Text value entered:: Happy")

        val submitButton = onView(
            allOf<View>(
                withId(R.id.getSentimentButton), withText("Submit"),
                withParent(withId(R.id.backgroundLayout)),
                isDisplayed()
            )
        )

        submitButton.perform(click())

        val idlingResource = OkHttp3IdlingResource.create("okhttp", HTTP_CLIENT)
        IdlingRegistry.getInstance().register(idlingResource)

        onView(withId(R.id.emojiView)).check(matches(isDisplayed()))
        reportHelper.label("Result Emoji Loaded")

        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @After
    fun tearDown() {
        reportHelper.label("Stopping App")
    }
}
