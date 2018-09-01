package com.example.victor.bakingapp.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;
import com.example.victor.bakingapp.objects.StepItem;

import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;
import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;
import com.example.victor.bakingapp.resources.Assets;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    static int recipeId = 0;
    static String recipeName = null;
    ArrayList<StepItem> stepItems;
    private Context context;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

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
            int totalHeight = height - actionBarHeight - statusBarHeight - Math.round(getResources().getDimension(R.dimen.container_regular_large)) - 100;
            backdropView.getLayoutParams().height = totalHeight;
        }

        //Set recipe image and text
        Bundle bundle;
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();

            recipeId = bundle.getInt(MainActivity.INTENT_RECIPE_ID);
            recipeName = bundle.getString(MainActivity.INTENT_RECIPE_NAME);

            ImageView recipeImageView = findViewById(R.id.recipe_background_view);
            recipeImageView.setImageResource(Assets.getCakeImages().get(recipeId - 1));
            TextView recipeNameView = findViewById(R.id.recipe_title);
            recipeNameView.setText(recipeName);
        } else {
            Log.e(LOG_TAG, "no extras received from the intent");
        }

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
                selection = IngredientsEntry.INGREDIENTS_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(context,
                        BakingContract.IngredientsEntry.INGREDIENTS_URI,
                        MainActivity.PROJECTION_INGREDIENTS,
                        selection,
                        selectionArgs,
                        null);
            case MainActivity.CURSOR_STEPS_LIST_LOADER_ID:
                selection = StepsEntry.STEPS_ID + "=?";
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
                RecipeFragmentServes recipeFragmentServes = new RecipeFragmentServes();
                recipeFragmentServes.setRecipeItem(data.getInt(data.getColumnIndex(RecipesEntry.RECIPES_SERVING)));
                recipeFragmentServes.setArguments(getIntent().getExtras());
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_fragment_container_serves, recipeFragmentServes)
                        .commit();
                break;
            case MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID:
                RecipeFragmentIngredients recipeFragmentIngredients = new RecipeFragmentIngredients();
                recipeFragmentIngredients.setIngredientItems(data);
                recipeFragmentIngredients.setArguments(getIntent().getExtras());
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_fragment_container_ingredients, recipeFragmentIngredients)
                        .commit();
                break;
            case MainActivity.CURSOR_STEPS_LIST_LOADER_ID:
                RecipeFragmentSteps recipeFragmentSteps = new RecipeFragmentSteps();
                recipeFragmentSteps.setStepItems(data);
                recipeFragmentSteps.setArguments(getIntent().getExtras());
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_fragment_container_steps, recipeFragmentSteps)
                        .commit();

                //Create the onClickListener and pass the stepItems parcelable into DetailActivity through intent
                TextView buttonView = findViewById(R.id.recipe_button_view);
                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stepItems = new ArrayList<>();
                        for (int i = 0; i < data.getCount(); i++) {
                            data.moveToPosition(i);
                            int stepId = data.getInt(data.getColumnIndex(StepsEntry.STEPS_ID));
                            String stepShortDescription = data.getString(data.getColumnIndex(StepsEntry.STEPS_SHORT_DESCRIPTION));
                            String stepDescription = data.getString(data.getColumnIndex(StepsEntry.STEPS_DESCRIPTION));
                            String stepVideoUrl = data.getString(data.getColumnIndex(StepsEntry.STEPS_VIDEO_URL));
                            String stepThumbnailUrl = data.getString(data.getColumnIndex(StepsEntry.STEPS_THUMBNAIL_URL));
                            stepItems.add(new StepItem(stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl));
                        }

                        Intent goToRecipeCookingActivity = new Intent(RecipeActivity.this, DetailActivity.class);
                        goToRecipeCookingActivity.putExtra(MainActivity.INTENT_RECIPE_ID, recipeId);
                        goToRecipeCookingActivity.putParcelableArrayListExtra(MainActivity.INTENT_STEP_ITEMS, stepItems);
                        startActivity(goToRecipeCookingActivity);
                    }
                });
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
