package com.example.victor.bakingapp.ui;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.victor.bakingapp.R;

import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeFragmentIngredients extends Fragment {
    private static final String LOG_TAG = RecipeFragmentServes.class.getSimpleName();
    private static final int INGREDIENTS_ID = 1000;
    private Cursor ingredientItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        LinearLayout ingredientsLayout = rootView.findViewById(R.id.recipe_ingredients_linear_layout);
        for (int i = 0; i < ingredientItems.getCount(); i++) {
            ingredientItems.moveToPosition(i);
            @SuppressLint("InflateParams") View view = LayoutInflater.from(rootView.getContext()).inflate(R.layout.layout_recipe_ingredients, null);
            view.setId(INGREDIENTS_ID + i);
            TextView ingredientsQuantityView = view.findViewById(R.id.layout_recipe_ingredients_quantity);
            TextView ingredientsMeasureView = view.findViewById(R.id.layout_recipe_ingredients_measure);
            TextView ingredientsIngredientView = view.findViewById(R.id.layout_recipe_ingredients_ingredient);
            ingredientsQuantityView.setText(ingredientItems.getString(ingredientItems.getColumnIndex(IngredientsEntry.INGREDIENTS_QUANTITY)));
            ingredientsMeasureView.setText(ingredientItems.getString(ingredientItems.getColumnIndex(IngredientsEntry.INGREDIENTS_MEASURE)));
            ingredientsIngredientView.setText(ingredientItems.getString(ingredientItems.getColumnIndex(IngredientsEntry.INGREDIENTS_INGREDIENT)));
            ingredientsLayout.addView(view);
        }
        return rootView;
    }

    public void setIngredientItems(Cursor ingredientItems) {
        this.ingredientItems = ingredientItems;
    }
}
