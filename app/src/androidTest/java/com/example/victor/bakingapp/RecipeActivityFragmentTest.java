package com.example.victor.bakingapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.victor.bakingapp.ui.MainActivity;
import com.example.victor.bakingapp.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

//EspressoImports

/******
 * Created by Victor on 9/30/2018.
 ******/

@RunWith(AndroidJUnit4.class)
public class RecipeActivityFragmentTest {

    private static final String RECIPE_NAME = "Nutella Pie";
    private static final int RECIPE_ID = 1;

    @Rule
    public final ActivityTestRule<RecipeActivity> recipeActivityActivityTestRule = new ActivityTestRule<>(RecipeActivity.class, true, false);

    @Test
    public void CheckRecipeName() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.INTENT_RECIPE_NAME, RECIPE_NAME);
        intent.putExtra(MainActivity.INTENT_RECIPE_ID, RECIPE_ID);
        recipeActivityActivityTestRule.launchActivity(intent);

        onView(withId(R.id.recipe_title)).check(matches(withText(RECIPE_NAME)));
    }
}
