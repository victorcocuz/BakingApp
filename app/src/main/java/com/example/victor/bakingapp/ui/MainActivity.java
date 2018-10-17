package com.example.victor.bakingapp.ui;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;
import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;
import com.example.victor.bakingapp.loaders.RecipeLoader;
import com.example.victor.bakingapp.objects.RecipeItem;
import com.example.victor.bakingapp.utilities.NetworkUtils;
import com.facebook.stetho.Stetho;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String INTENT_RECIPE_IMAGE_URL = "recipeImage";
    public static final String INTENT_RECIPE_ID = "recipeId";
    public static final String INTENT_RECIPE_NAME = "recipeName";
    public static final String INTENT_STEP_ID = "stepID";
    public static final String INTENT_STEP_ITEMS = "stepItems";
    //Projections
    public static final String[] PROJECTION_RECIPE = new String[]{
            RecipesEntry.RECIPES_ID,
            RecipesEntry.RECIPES_NAME,
            RecipesEntry.RECIPES_SERVING,
            RecipesEntry.RECIPES_IMAGE};

    MainFragment recipeListFragment = new MainFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    //Loader IDs
    public static final int MAIN_LIST_LOADER_ID = 1000;
    public static final int CURSOR_RECIPE_LOADER_ID = 2000;
    public static final int CURSOR_INGREDIENTS_LIST_LOADER_ID = 3000;
    public static final int CURSOR_STEPS_LIST_LOADER_ID = 4000;
    public static final String[] PROJECTION_INGREDIENTS = new String[]{
            IngredientsEntry.INGREDIENTS_RECIPE_ID,
            IngredientsEntry.INGREDIENTS_ID,
            IngredientsEntry.INGREDIENTS_REAL_QUANTITY,
            IngredientsEntry.INGREDIENTS_QUANTITY,
            IngredientsEntry.INGREDIENTS_MEASURE,
            IngredientsEntry.INGREDIENTS_INGREDIENT};
    public static final String[] PROJECTION_STEPS = new String[]{
            StepsEntry.STEPS_RECIPE_ID,
            StepsEntry.STEPS_ID,
            StepsEntry.STEPS_SHORT_DESCRIPTION,
            StepsEntry.STEPS_DESCRIPTION,
            StepsEntry.STEPS_VIDEO_URL,
            StepsEntry.STEPS_THUMBNAIL_URL};
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        mSavedInstanceState = savedInstanceState;

        if (mSavedInstanceState != null) {
            getLoaderManager().restartLoader(CURSOR_RECIPE_LOADER_ID, null, this).forceLoad();
        } else {
            getLoaderManager().initLoader(CURSOR_RECIPE_LOADER_ID, null, this).forceLoad();
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CURSOR_RECIPE_LOADER_ID:
                return new CursorLoader(this,
                        BakingContract.RecipesEntry.RECIPES_URI,
                        PROJECTION_RECIPE,
                        null,
                        null,
                        null);
            case MAIN_LIST_LOADER_ID:
                //Recipe Loader is used to retrieve data from json. It can only work if the device is connected to internet
                if (NetworkUtils.isConnectedToInternet(this)) {
                    return new RecipeLoader(this);
                } else {
                    Log.e(LOG_TAG, "Please connect device to internet");
                    Toast.makeText(this, "Please connect device to internet", Toast.LENGTH_SHORT).show();
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case CURSOR_RECIPE_LOADER_ID:
                Cursor cursor = (Cursor) data;
                if (cursor.getCount() > 0) {
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_list_fragment_container, recipeListFragment)
                            .commit();
                } else {
                    getLoaderManager().initLoader(MAIN_LIST_LOADER_ID, null, this);
                }
                break;
            case MAIN_LIST_LOADER_ID:
                List<RecipeItem> recipeItems = (List<RecipeItem>) data;
                initialize(recipeItems);
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_list_fragment_container, recipeListFragment)
                        .commit();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        fragmentManager.beginTransaction().remove(recipeListFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSavedInstanceState != null) {
            getLoaderManager().restartLoader(CURSOR_RECIPE_LOADER_ID, null, this).forceLoad();
        }
    }

    public void initialize(List<RecipeItem> data) {
        for (int i = 0; i < data.size(); i++) {
            RecipeItem recipe = data.get(i);
            int id = recipe.getRecipeId();
            String[] imageUrlList = getApplicationContext().getResources().getStringArray(R.array.list_images_url);
            ContentValues recipesValues = new ContentValues();
            recipesValues.put(RecipesEntry.RECIPES_ID, id);
            recipesValues.put(RecipesEntry.RECIPES_NAME, recipe.getRecipeName());
            recipesValues.put(RecipesEntry.RECIPES_SERVING, recipe.getRecipeServing());
            recipesValues.put(RecipesEntry.RECIPES_IMAGE, imageUrlList[i]);
            getContentResolver().insert(RecipesEntry.RECIPES_URI, recipesValues);

            int ingredientId = 0;
            for (int j = 0; j < recipe.getRecipeIngredientItems().size(); j++) {
                ContentValues ingredientsValues = new ContentValues();
                ingredientsValues.put(IngredientsEntry.INGREDIENTS_RECIPE_ID, id);
                ingredientsValues.put(IngredientsEntry.INGREDIENTS_ID, ingredientId);
                ingredientsValues.put(IngredientsEntry.INGREDIENTS_QUANTITY, recipe.getRecipeIngredientItems().get(j).getIngredientQuantity());
                ingredientsValues.put(IngredientsEntry.INGREDIENTS_MEASURE, recipe.getRecipeIngredientItems().get(j).getIngredientMeasure());
                ingredientsValues.put(IngredientsEntry.INGREDIENTS_INGREDIENT, recipe.getRecipeIngredientItems().get(j).getIngredientIngredient());
                getContentResolver().insert(IngredientsEntry.INGREDIENTS_URI, ingredientsValues);
                ingredientId++;
            }

            int stepId = 0;
            for (int k = 0; k < recipe.getRecipeStepItems().size(); k++) {
                ContentValues stepsValues = new ContentValues();
                stepsValues.put(StepsEntry.STEPS_RECIPE_ID, id);
                stepsValues.put(StepsEntry.STEPS_ID, stepId);
                stepsValues.put(StepsEntry.STEPS_SHORT_DESCRIPTION, recipe.getRecipeStepItems().get(k).getStepShortDescription());
                stepsValues.put(StepsEntry.STEPS_DESCRIPTION, recipe.getRecipeStepItems().get(k).getStepDescription());
                stepsValues.put(StepsEntry.STEPS_VIDEO_URL, recipe.getRecipeStepItems().get(k).getStepVideoUrl());
                stepsValues.put(StepsEntry.STEPS_THUMBNAIL_URL, recipe.getRecipeStepItems().get(k).getStepThumbnailUrl());
                getContentResolver().insert(StepsEntry.STEPS_URI, stepsValues);
                stepId++;
            }
        }
    }
}
