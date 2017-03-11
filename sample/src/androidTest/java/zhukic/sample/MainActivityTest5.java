package zhukic.sample;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import zhukic.sectionedrecyclerview.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest5 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest5() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_name), isDisplayed()));
        appCompatEditText.perform(replaceText("District 9"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_genre), isDisplayed()));
        appCompatEditText2.perform(replaceText("Sci-Fi"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_year), isDisplayed()));
        appCompatEditText3.perform(replaceText("2009"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withId(R.id.buttonPanel),
                                withParent(withId(R.id.parentPanel)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.subheaderText), withText("D"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        6),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("D")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.movieName), withText("District 9"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        7),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("District 9")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.movieName), withText("Catch Me If You Can"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        5),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Catch Me If You Can")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.subheaderText), withText("C"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        4),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("C")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.subheaderText), withText("F"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        8),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("F")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
