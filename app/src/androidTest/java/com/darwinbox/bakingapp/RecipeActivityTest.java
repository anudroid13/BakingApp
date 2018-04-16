package com.darwinbox.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.darwinbox.bakingapp.activities.RecipeActivity;
import com.darwinbox.bakingapp.fragments.RecipeFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mTestRule
            = new ActivityTestRule<>(RecipeActivity.class);
private static final String TAG = "test_anudroid";
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = ((RecipeFragment)mTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.recipe_fragment_body_part)).getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void testRecyclerViewItemClicked_NextActivityIsDisplayed(){
        Log.d(TAG ,idlingResource.isIdleNow() +"===");
        onView(withId(R.id.recipe_recycler)).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Log.d(TAG ,idlingResource.isIdleNow() +"===");
        onView(withId(R.id.recipe_ingredient_recycler)).check(matches(isDisplayed()));

        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerView_hasRecipeNameDisplayed(){
        Log.d(TAG ,idlingResource.isIdleNow() +"===");
        onView(withText("Cheesecake")).check(matches(isDisplayed()));

        Log.d(TAG ,idlingResource.isIdleNow() +"===");
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}
