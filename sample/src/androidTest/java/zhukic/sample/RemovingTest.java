package zhukic.sample;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import zhukic.sectionedrecyclerview.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RemovingTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void removingTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView4.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView5.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction recyclerView6 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView6.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView7 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView7.perform(actionOnItemAtPosition(19, click()));

        ViewInteraction recyclerView8 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView8.perform(actionOnItemAtPosition(18, click()));

        ViewInteraction recyclerView9 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView9.perform(actionOnItemAtPosition(14, click()));

        ViewInteraction recyclerView10 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView10.perform(actionOnItemAtPosition(12, click()));

        ViewInteraction recyclerView11 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView11.perform(actionOnItemAtPosition(10, click()));

        ViewInteraction recyclerView12 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView12.perform(actionOnItemAtPosition(8, click()));

        ViewInteraction recyclerView13 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView13.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction recyclerView14 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView14.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction recyclerView15 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView15.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction recyclerView16 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView16.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView17 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView17.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView18 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView18.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView19 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView19.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView20 = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView20.perform(actionOnItemAtPosition(1, click()));

    }

}
