package com.example.victor.bakingapp.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;
import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;
import com.example.victor.bakingapp.objects.StepItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    private static final String ON_SAVED_INSTANCE_RECIPE_NAME = "onSavedInstanceRecipeName";
    private static final String ON_SAVED_INSTANCE_RECIPE_ID = "onSavedInstanceRecipeId";
    private static final String ON_SAVED_INSTANCE_RECIPE_IMAGE_URL = "onSavedInstanceRecipeImageUrl";
    static String recipeImageUrl = null;
    static int recipeId = 0;
    static String recipeName = null;
    private Bundle mSavedInstanceState;
    ArrayList<StepItem> stepItems;
    private Context context;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private boolean twoPanes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mSavedInstanceState = savedInstanceState;
        twoPanes = findViewById(R.id.recipe_general_tablet_layout) != null;
        context = getApplicationContext();

        //Set background drop height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            View backdropView = findViewById(R.id.recipe_background_drop);
            int actionBarHeight = 0;
            int actionBarResourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (actionBarResourceId > 0) {
                actionBarHeight = getResources().getDimensionPixelSize(actionBarResourceId);
            }
            int statusBarHeight = 0;
            int statusBarResourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (statusBarResourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(statusBarResourceId);
            }
            int totalHeight = height - actionBarHeight - statusBarHeight - Math.round(getResources().getDimension(R.dimen.container_regular_medium) + 100);
            backdropView.getLayoutParams().height = totalHeight;
        }

        //Set recipe image and text
        Bundle bundle;
        if (savedInstanceState != null) {
            recipeName = savedInstanceState.getString(ON_SAVED_INSTANCE_RECIPE_NAME);
            recipeId = savedInstanceState.getInt(ON_SAVED_INSTANCE_RECIPE_ID);
            recipeImageUrl = savedInstanceState.getString(ON_SAVED_INSTANCE_RECIPE_IMAGE_URL);
        } else {
            if (getIntent().getExtras() != null) {
                bundle = getIntent().getExtras();
                recipeId = bundle.getInt(MainActivity.INTENT_RECIPE_ID);
                recipeName = bundle.getString(MainActivity.INTENT_RECIPE_NAME);
                recipeImageUrl = bundle.getString(MainActivity.INTENT_RECIPE_IMAGE_URL);
            } else {
                Log.e(LOG_TAG, "no extras received from the intent");
            }
        }
        ImageView recipeImageView = findViewById(R.id.recipe_background_view);
        Picasso.get().load(recipeImageUrl).into(recipeImageView);
        TextView recipeNameView = findViewById(R.id.recipe_title);
        recipeNameView.setText(recipeName);

        //Initialize loaders to get cursor data
        getLoaderManager().initLoader(MainActivity.CURSOR_RECIPE_LOADER_ID, null, RecipeActivity.this);
        getLoaderManager().initLoader(MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID, null, RecipeActivity.this);
        getLoaderManager().initLoader(MainActivity.CURSOR_STEPS_LIST_LOADER_ID, null, RecipeActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;

        switch (id) {
            case MainActivity.CURSOR_RECIPE_LOADER_ID:
                selection = RecipesEntry.RECIPES_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(context,
                        RecipesEntry.RECIPES_URI,
                        MainActivity.PROJECTION_RECIPE,
                        selection,
                        selectionArgs,
                        null);
            case MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID:
                selection = IngredientsEntry.INGREDIENTS_RECIPE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(context,
                        BakingContract.IngredientsEntry.INGREDIENTS_URI,
                        MainActivity.PROJECTION_INGREDIENTS,
                        selection,
                        selectionArgs,
                        null);
            case MainActivity.CURSOR_STEPS_LIST_LOADER_ID:
                selection = StepsEntry.STEPS_RECIPE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(context,
                        BakingContract.StepsEntry.STEPS_URI,
                        MainActivity.PROJECTION_STEPS,
                        selection,
                        selectionArgs,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        switch (loader.getId()) {
            case MainActivity.CURSOR_RECIPE_LOADER_ID:
                data.moveToFirst();
                //Create recipeFragmentServes if there is no SavedInstanceState else replace
                if (mSavedInstanceState == null) {
                    RecipeFragmentServes recipeFragmentServes = new RecipeFragmentServes();
                    recipeFragmentServes.setRecipeItem(data.getInt(data.getColumnIndex(RecipesEntry.RECIPES_SERVING)));
                    recipeFragmentServes.setRecipeId(data.getInt(data.getColumnIndex(RecipesEntry.RECIPES_ID)));
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_fragment_container_serves, recipeFragmentServes)
                            .commit();
                }
                break;
            case MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID:
                //Create recipeFragmentIngredients if there is no SavedInstanceState else replace
                if (mSavedInstanceState == null) {
                    RecipeFragmentIngredients recipeFragmentIngredients = new RecipeFragmentIngredients();
                    recipeFragmentIngredients.setIngredientItems(data);
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_fragment_container_ingredients, recipeFragmentIngredients)
                            .commit();
                } else {
                    RecipeFragmentIngredients recipeFragmentIngredients = new RecipeFragmentIngredients();
                    recipeFragmentIngredients.setIngredientItems(data);
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_fragment_container_ingredients, recipeFragmentIngredients)
                            .commit();
                }
                break;
            case MainActivity.CURSOR_STEPS_LIST_LOADER_ID:
                //Create stepItems ArrayList to pass for DetailVideosFragment
                TextView buttonView = findViewById(R.id.recipe_button_view);
                View fillerView = findViewById(R.id.recipe_container_filler);
                stepItems = new ArrayList<>();
                for (int i = 0; i < data.getCount(); i++) {
                    data.moveToPosition(i);
                    int stepId = data.getInt(data.getColumnIndex(StepsEntry.STEPS_RECIPE_ID));
                    String stepShortDescription = data.getString(data.getColumnIndex(StepsEntry.STEPS_SHORT_DESCRIPTION));
                    String stepDescription = data.getString(data.getColumnIndex(StepsEntry.STEPS_DESCRIPTION));
                    String stepVideoUrl = data.getString(data.getColumnIndex(StepsEntry.STEPS_VIDEO_URL));
                    String stepThumbnailUrl = data.getString(data.getColumnIndex(StepsEntry.STEPS_THUMBNAIL_URL));
                    stepItems.add(new StepItem(stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl));
                }

                //Create RecipeFragmentSteps if there is no SavedInstanceState else replace
                buttonView.setVisibility(View.VISIBLE);
                fillerView.setVisibility(View.VISIBLE);
                if (mSavedInstanceState == null) {
                    RecipeFragmentSteps recipeFragmentSteps = new RecipeFragmentSteps();
                    recipeFragmentSteps.setStepItems(stepItems);
                    recipeFragmentSteps.setTwoPanes(twoPanes);
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_fragment_container_steps, recipeFragmentSteps)
                            .commit();
                } else {
                    RecipeFragmentSteps recipeFragmentSteps = new RecipeFragmentSteps();
                    recipeFragmentSteps.setStepItems(stepItems);
                    recipeFragmentSteps.setTwoPanes(twoPanes);
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_fragment_container_steps, recipeFragmentSteps)
                            .commit();
                }

                //Set DetailActivity for onePane and set DetailVideoActivity for twoPanes
                if (!twoPanes) {
                    buttonView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent goToRecipeCookingActivity = new Intent(RecipeActivity.this, DetailActivity.class);
                            goToRecipeCookingActivity.putExtra(MainActivity.INTENT_RECIPE_ID, recipeId);
                            goToRecipeCookingActivity.putParcelableArrayListExtra(MainActivity.INTENT_STEP_ITEMS, stepItems);
                            startActivity(goToRecipeCookingActivity);
                        }
                    });
                }
                if (twoPanes) {
                    buttonView.setVisibility(View.GONE);
                    fillerView.setVisibility(View.GONE);
                    if (mSavedInstanceState == null) {
                        DetailVideosFragment newRecipeVideosFragment = new DetailVideosFragment();
                        newRecipeVideosFragment.setStepIndex(0);
                        newRecipeVideosFragment.setStepItems(stepItems);

                        if (mSavedInstanceState == null) {
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_video_container_test, newRecipeVideosFragment)
                                    .commit();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ON_SAVED_INSTANCE_RECIPE_NAME, recipeName);
        outState.putInt(ON_SAVED_INSTANCE_RECIPE_ID, recipeId);
        outState.putString(ON_SAVED_INSTANCE_RECIPE_IMAGE_URL, recipeImageUrl);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
