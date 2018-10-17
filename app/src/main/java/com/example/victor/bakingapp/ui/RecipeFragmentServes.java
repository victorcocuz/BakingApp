package com.example.victor.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;
import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeFragmentServes extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = RecipeFragmentServes.class.getSimpleName();

    //Saved Instance
    private static final String ON_SAVED_INSTANCE_SERVES_NUMBER = "servesNumber";
    private static final String ON_SAVED_INSTANCE_SERVES_UPDATED = "servesUpdatedBoolean";
    private static final String ON_SAVED_INSTANCE_RECIPE_ID = "recipeId";
    private int recipeServings;
    TextView recipeServeView;
    private int recipeId;
    //Ingredients calculation and update
    private int recipeInitialServings;
    private float recipeAdjustingFactor;
    private boolean servesUpdated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_serves, container, false);

        if (savedInstanceState != null) {
            recipeServings = savedInstanceState.getInt(ON_SAVED_INSTANCE_SERVES_NUMBER);
            servesUpdated = savedInstanceState.getBoolean(ON_SAVED_INSTANCE_SERVES_UPDATED);
            recipeId = savedInstanceState.getInt(ON_SAVED_INSTANCE_RECIPE_ID);
        }

        recipeServeView = rootView.findViewById(R.id.recipe_serves_text);
        recipeServeView.setText(String.valueOf(recipeServings));

        LinearLayout recipeServeRemove = rootView.findViewById(R.id.recipe_serves_icon_remove);
        recipeServeRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeServings > 1) {
                    recipeInitialServings = recipeServings;
                    recipeServings--;
                    adjustServings(recipeServings);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_minimum_servings), Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout recipeServeAdd = rootView.findViewById(R.id.recipe_serves_icon_add);
        recipeServeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeServings < 12) {
                    recipeInitialServings = recipeServings;
                    recipeServings++;
                    adjustServings(recipeServings);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_maximum_servings), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void setRecipeItem(int recipeServings) {
        this.recipeServings = recipeServings;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(IngredientsEntry.INGREDIENTS_URI, null);
        }
        String selection = IngredientsEntry.INGREDIENTS_RECIPE_ID + " =? ";
        String[] selectionArgs = new String[]{String.valueOf(recipeId)};
        return new CursorLoader(getContext(),
                BakingContract.IngredientsEntry.INGREDIENTS_URI,
                MainActivity.PROJECTION_INGREDIENTS,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished
            (@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!servesUpdated) {
            servesUpdated = true;
            updateIngredientQuantities(data);
            getLoaderManager().restartLoader(MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID, null, RecipeFragmentServes.this);
        } else {
            servesUpdated = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
    }

    public void adjustServings(int recipeServings) {
        ContentValues contentValuesRecipe = new ContentValues();
        contentValuesRecipe.put(RecipesEntry.RECIPES_SERVING, recipeServings);
        String selectionRecipe = RecipesEntry.RECIPES_ID + " =? ";
        String[] selectionArgsRecipe = new String[]{String.valueOf(recipeId)};
        if (getActivity() != null) {
            getActivity().getContentResolver().update(
                    RecipesEntry.RECIPES_URI,
                    contentValuesRecipe,
                    selectionRecipe,
                    selectionArgsRecipe);
        }

        recipeServeView.setText(String.valueOf(recipeServings));
        recipeAdjustingFactor = (float) recipeServings / (float) recipeInitialServings;
        getLoaderManager().initLoader(MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID, null, RecipeFragmentServes.this);
    }

    public void updateIngredientQuantities(Cursor data) {
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            float initialQuantity;
            if (data.getFloat(data.getColumnIndex(IngredientsEntry.INGREDIENTS_REAL_QUANTITY)) != 0) {
                initialQuantity = data.getFloat(data.getColumnIndex(IngredientsEntry.INGREDIENTS_REAL_QUANTITY));
            } else {
                initialQuantity = data.getFloat(data.getColumnIndex(IngredientsEntry.INGREDIENTS_QUANTITY));
            }
            float currentQuantity = initialQuantity * recipeAdjustingFactor;
            @SuppressLint("DefaultLocale") String stringQuantity = String.format("%.1f", currentQuantity);
            ContentValues contentValuesIngredients = new ContentValues();
            contentValuesIngredients.put(IngredientsEntry.INGREDIENTS_REAL_QUANTITY, currentQuantity);
            contentValuesIngredients.put(IngredientsEntry.INGREDIENTS_QUANTITY, stringQuantity);
            String selectionIngredients = IngredientsEntry.INGREDIENTS_RECIPE_ID + " =? " + " and " + IngredientsEntry.INGREDIENTS_ID + " =? ";
            String[] selectionArgsIngredients = new String[]{String.valueOf(recipeId), String.valueOf(i)};
            if (getActivity() != null) {
                getActivity().getContentResolver().update(
                        IngredientsEntry.INGREDIENTS_URI,
                        contentValuesIngredients,
                        selectionIngredients,
                        selectionArgsIngredients);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ON_SAVED_INSTANCE_SERVES_NUMBER, recipeServings);
        outState.putBoolean(ON_SAVED_INSTANCE_SERVES_UPDATED, servesUpdated);
        outState.putInt(ON_SAVED_INSTANCE_RECIPE_ID, recipeId);
    }
}
