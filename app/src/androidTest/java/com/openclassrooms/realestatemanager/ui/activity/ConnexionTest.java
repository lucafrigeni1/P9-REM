package com.openclassrooms.realestatemanager.ui.activity;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ConnexionTest {

   @Rule
   public ActivityTestRule<AuthenticationActivity> mActivityTestRule = new ActivityTestRule<>(AuthenticationActivity.class);
//
   @Test
   public void authenticationActivityTest() {
       ViewInteraction supportVectorDrawablesButton = onView(
               allOf(withText("Sign in with Google"),
                       childAtPosition(
                               allOf(withId(R.id.btn_holder),
                                       childAtPosition(
                                               withId(R.id.container),
                                               0)),
                               1)));
       supportVectorDrawablesButton.perform(scrollTo(), click());
//
       ViewInteraction imageView = onView(
               allOf(withId(R.id.logo), withContentDescription("App logo"),
                       withParent(allOf(withId(R.id.root),
                               withParent(withId(android.R.id.content)))),
                       isDisplayed()));
       imageView.check(matches(isDisplayed()));
       //onView(withId(R.id.logo)).check(matches(isDisplayed()));
   }
//
   private static Matcher<View> childAtPosition(
           final Matcher<View> parentMatcher, final int position) {
//
       return new TypeSafeMatcher<View>() {
           @Override
           public void describeTo(Description description) {
               description.appendText("Child at position " + position + " in parent ");
               parentMatcher.describeTo(description);
           }
//
           @Override
           public boolean matchesSafely(View view) {
               ViewParent parent = view.getParent();
               return parent instanceof ViewGroup && parentMatcher.matches(parent)
                       && view.equals(((ViewGroup) parent).getChildAt(position));
           }
       };
   }
}
