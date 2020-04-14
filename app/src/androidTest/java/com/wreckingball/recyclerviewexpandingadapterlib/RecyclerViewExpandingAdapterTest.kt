package com.wreckingball.recyclerviewexpandingadapterlib

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.wreckingball.recyclerviewexpandingadapter.RecyclerViewExpandingAdapter

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RecyclerViewExpandingAdapterTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var adapter: RecyclerViewExpandingAdapter
    private var originalItemCount: Int = 0

    @Before
    fun setup() {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recyclerViewExpanding)
        adapter = recyclerView.adapter as RecyclerViewExpandingAdapter
        originalItemCount = recyclerView.adapter!!.itemCount
    }

    @Test
    fun testOpenCloseOneItem() {
        assertTrue(20 == originalItemCount)

        //expand parent 0
        onView(withId(R.id.recyclerViewExpanding))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        var newCount = adapter.itemCount
        assertTrue(25 == newCount)

        //close parent 0
        onView(withId(R.id.recyclerViewExpanding))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        newCount = adapter.itemCount
        assertTrue(20 == newCount)
    }

    @Test
    fun testOpenOneItemCloseDifferent() {
        assertTrue(20 == originalItemCount)

        //expand parent 0
        onView(withId(R.id.recyclerViewExpanding))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        var newCount = adapter.itemCount
        assertTrue(25 == newCount)

        onView(withId(R.id.recyclerViewExpanding))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(14))

        //expand parent 14
        onView(withId(R.id.recyclerViewExpanding))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(19, click()))
        newCount = adapter.itemCount
        assertTrue(25 == newCount)

        onView(RecyclerViewMatcher(R.id.recyclerViewExpanding)
            .atPositionOnView(14, R.id.parent_title))
            .check(matches(isDisplayed()))

        onView(RecyclerViewMatcher(R.id.recyclerViewExpanding)
            .atPositionOnView(14, R.id.parent_title))
            .check(matches(withText("Hello there, parent 14!")))
    }

    @Test
    fun testLastItemOpen() {
        assertTrue(20 == originalItemCount)

        //expand parent 19
        onView(withId(R.id.recyclerViewExpanding))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(19, click()))
        var newCount = adapter.itemCount
        assertTrue(25 == newCount)

        onView(RecyclerViewMatcher(R.id.recyclerViewExpanding)
            .atPositionOnView(20, R.id.child_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.wreckingball.recyclerviewexpandingadapterlib", appContext.packageName)
    }
}
