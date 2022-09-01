package co.elastic.apm.opbeans.utils

import android.view.View
import androidx.core.view.isVisible
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher
import java.lang.Thread.sleep

object EspressoUtils {

    fun waitForView(
        viewMatcher: Matcher<View>,
        waitMillis: Int = 5000,
        waitMillisPerTry: Long = 100
    ): ViewInteraction {
        val maxTries = waitMillis / waitMillisPerTry.toInt()
        var tries = 0

        for (i in 0..maxTries)
            try {
                tries++
                onView(isRoot()).perform(SearchViewAction(viewMatcher))
                return onView(viewMatcher)
            } catch (e: Exception) {
                if (tries == maxTries) {
                    throw e
                }
                sleep(waitMillisPerTry)
            }
        throw Exception("Error finding a view matching $viewMatcher")
    }

    class SearchViewAction(private val matcher: Matcher<View>) : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "searching for view $matcher in the root view"
        }

        override fun perform(uiController: UiController?, view: View?) {
            var tries = 0
            val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)

            childViews.forEach {
                tries++
                if (matcher.matches(it).and(it.isVisible)) {
                    // found the view
                    return
                }
            }

            throw NoMatchingViewException.Builder()
                .withRootView(view)
                .withViewMatcher(matcher)
                .build()
        }
    }

}