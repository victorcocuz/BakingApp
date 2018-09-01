package com.example.victor.bakingapp.ui;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;

import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;
import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;
import com.example.victor.bakingapp.objects.StepItem;
import com.example.victor.bakingapp.resources.Assets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

/******
 * Created by Victor on 8/19/2018.
 ******/
public class RecipeMasterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RecipeMasterFragment.class.getSimpleName();
    private static final int INGREDIENTS_ID = 1000;
    private static final int STEPS_ID = 2000;

    View rootView;
    int recipeId = 0;

    OnStartCookingClickListener onStartCookingClickListener;

    public interface OnStartCookingClickListener {
        void onButtonSelected(int recipeId, Cursor data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onStartCookingClickListener = (OnStartCookingClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_master, container, false);
        ButterKnife.bind(this, rootView);

        //Set background drop height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            View backdropView = rootView.findViewById(R.id.recipe_master_background_drop);
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

        if (getActivity().getIntent() != null && getActivity().getIntent().getExtras().getInt(MainActivity.INTENT_RECIPE_ID) != 0) {
            recipeId = getActivity().getIntent().getExtras().getInt(MainActivity.INTENT_RECIPE_ID);
        }

        getActivity().getLoaderManager().initLoader(MainActivity.CURSOR_RECIPE_LOADER_ID, null, RecipeMasterFragment.this);
        getActivity().getLoaderManager().initLoader(MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID, null, RecipeMasterFragment.this);
        getActivity().getLoaderManager().initLoader(MainActivity.CURSOR_STEPS_LIST_LOADER_ID, null, RecipeMasterFragment.this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;

        switch (id) {
            case MainActivity.CURSOR_RECIPE_LOADER_ID:
                selection = RecipesEntry.RECIPES_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(getContext(),
                        RecipesEntry.RECIPES_URI,
                        MainActivity.PROJECTION_RECIPE,
                        selection,
                        selectionArgs,
                        null);
            case MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID:
                selection = IngredientsEntry.INGREDIENTS_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(getContext(),
                        BakingContract.IngredientsEntry.INGREDIENTS_URI,
                        MainActivity.PROJECTION_INGREDIENTS,
                        selection,
                        selectionArgs,
                        null);
            case MainActivity.CURSOR_STEPS_LIST_LOADER_ID:
                selection = StepsEntry.STEPS_ID + "=?";
                selectionArgs = new String[]{String.valueOf(recipeId)};
                return new CursorLoader(getContext(),
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
                loadRecipe(data, rootView);
                break;
            case MainActivity.CURSOR_INGREDIENTS_LIST_LOADER_ID:
                loadIngredients(data, rootView);
                break;
            case MainActivity.CURSOR_STEPS_LIST_LOADER_ID:
                loadSteps(data, rootView);

                TextView startCookingView = rootView.findViewById(R.id.recipe_master_button_view);
                startCookingView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStartCookingClickListener.onButtonSelected(recipeId, data);
                    }
                });
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void loadRecipe(Cursor data, View rootView) {
        data.moveToFirst();
        ImageView recipeImageView = rootView.findViewById(R.id.recipe_master_background_view);
        recipeImageView.setImageResource(Assets.getCakeImages().get(recipeId - 1));
        TextView recipeNameView = rootView.findViewById(R.id.recipe_master_title);
        TextView recipeServeView = rootView.findViewById(R.id.recipe_master_text_serves);
        recipeNameView.setText(data.getString(data.getColumnIndex(RecipesEntry.RECIPES_NAME)));
        recipeServeView.setText(data.getString(data.getColumnIndex(RecipesEntry.RECIPES_SERVING)));
    }

    private void loadIngredients(Cursor data, View rootView) {
        LinearLayout ingredientsLayout = rootView.findViewById(R.id.recipe_master_ingredients_linear_layout);
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_recipe_master_ingredients, null);
            view.setId(INGREDIENTS_ID + i);
            TextView ingredientsQuantityView = view.findViewById(R.id.layout_recipe_master_ingredients_quantity);
            TextView ingredientsMeasureView = view.findViewById(R.id.layout_recipe_master_ingredients_measure);
            TextView ingredientsIngredientView = view.findViewById(R.id.layout_recipe_master_ingredients_ingredient);
            ingredientsQuantityView.setText(data.getString(data.getColumnIndex(IngredientsEntry.INGREDIENTS_QUANTITY)));
            ingredientsMeasureView.setText(data.getString(data.getColumnIndex(IngredientsEntry.INGREDIENTS_MEASURE)));
            ingredientsIngredientView.setText(data.getString(data.getColumnIndex(IngredientsEntry.INGREDIENTS_INGREDIENT)));
            ingredientsLayout.addView(view);
        }
    }

    private void loadSteps(Cursor data, View rootView) {
        LinearLayout stepsLayout = rootView.findViewById(R.id.recipe_master_steps_linear_layout);
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);

            //Populate steps as linear layout in master fragment
            @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_recipe_master_steps, null);
            view.setId(STEPS_ID + i);
            TextView stepsTitleView = view.findViewById(R.id.layout_recipe_master_steps_title);
            TextView stepsNumberView = view.findViewById(R.id.layout_recipe_master_steps_number);
            TextView stepsShortDescriptionView = view.findViewById(R.id.layout_recipe_master_steps_short_description);
            TextView stepsDescriptionView = view.findViewById(R.id.layout_recipe_master_steps_description);
            stepsTitleView.setText(rootView.getResources().getString(R.string.recipe_individual_step));
            stepsNumberView.setText(String.valueOf(i + 1));
            String stepsShortDescription = " - " + data.getString(data.getColumnIndex(StepsEntry.STEPS_SHORT_DESCRIPTION));
            stepsShortDescriptionView.setText(stepsShortDescription);
            if (i > 0) {
                stepsDescriptionView.setText(data.getString(data.getColumnIndex(StepsEntry.STEPS_DESCRIPTION)));
            }
            stepsLayout.addView(view);
        }
    }
}
