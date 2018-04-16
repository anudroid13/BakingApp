package com.darwinbox.bakingapp;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.darwinbox.bakingapp.activities.RecipeActivity;
import com.darwinbox.bakingapp.activities.RecipeDetailActivity;
import com.darwinbox.bakingapp.models.Recipe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityIntentTest {

    @Rule
    public IntentsTestRule<RecipeActivity> recipeActivityActivityTestRule = new IntentsTestRule<>
            (RecipeActivity.class);
    private static final String RECIPE_KEY = "selected_recipe_key";

    @Test
    public void clickRecipe_testIntentToNextActivity() {
        int recipeId = 2;
        onView(withId(R.id.recipe_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition
                (1, click()));

        intended(hasComponent(ComponentName.createRelative(
                recipeActivityActivityTestRule.getActivity()
                , RecipeDetailActivity.class.getName())));

        Matcher<Bundle> bundleMatcher = hasEntry(RECIPE_KEY, recipeId);
        intended(hasExtras(bundleMatcher));
    }

    public static Matcher<Bundle> hasEntry(final String key,final int value) {
        return new TypeSafeMatcher<Bundle>() {
            @Override
            protected boolean matchesSafely(Bundle item) {
                //get the recipeName from bundle.
                int recipeId = ((Recipe) item.get(RECIPE_KEY)).getId();

                return recipeId != 0 && recipeId == value;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has bundle with: key: " +key);
                description.appendText(" value: " + value);
            }
        };
    }
}
