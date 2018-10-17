package com.example.victor.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.victor.bakingapp.ui.MainActivity;
import com.example.victor.bakingapp.ui.MainFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

//Espresso imports

/******
 * Created by Victor on 9/26/2018.
 ******/
@RunWith(AndroidJUnit4.class)
public class MainFragmentBasicTest {
    //Find view
    //Perform action on view
    //Check if view does what you expected

    private static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensRecipeActivity() {
        mainActivityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainFragment fragment = new MainFragment();
                mainActivityTestRule.getActivity().getSupportFragmentManager()
                        .beginTransaction().add(R.id.recipe_list_fragment_container, fragment)
                        .commit();
            }
        });
        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific recyclerview item and clicks it
        onData(anything()).inAdapterView(withId(R.id.fragment_recipes_list_recycler_view)).atPosition(1).perform(click());

        //Check that the RecipeActivity opens with the correct recipe name displayed
        onView(withId(R.id.recipe_title)).check(matches(withText(RECIPE_NAME)));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}
