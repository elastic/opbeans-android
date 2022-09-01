package co.elastic.apm.opbeans


import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import co.elastic.apm.opbeans.utils.DispatcherIdlerRule
import co.elastic.apm.opbeans.utils.EspressoUtils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    val activityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Rule
    @JvmField
    val dispatcherIdlerRule = DispatcherIdlerRule()

    @Test
    fun testOneProductCheckout() {
        onView(withId(R.id.list)).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(withId(R.id.add_to_cart_fab)).perform(click())

        pressBack()

        onView(withId(R.id.shopping_cart)).perform(click())

        onView(withId(R.id.cart_checkout_option)).perform(click())

        EspressoUtils.waitForView(withId(R.id.empty_list_container))

        onView(withId(R.id.empty_list_message)).check(matches(isDisplayed()))
    }
}
