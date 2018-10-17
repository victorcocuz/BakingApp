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
import com.example.victor.bakingapp.objects.IngredientItem;

import java.util.ArrayList;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeFragmentIngredients extends Fragment {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = RecipeFragmentServes.class.getSimpleName();
    private static final String ON_SAVED_INSTANCE_INGREDIENT_ITEMS = "ingredientItems";
    private static final int INGREDIENTS_ID = 1000;
    private Cursor ingredientItems;
    private ArrayList<IngredientItem> ingredientItemsForSavedState = new ArrayList<>();
    private int ingredientsSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        if (savedInstanceState != null) {
            ingredientItemsForSavedState = savedInstanceState.getParcelableArrayList(ON_SAVED_INSTANCE_INGREDIENT_ITEMS);
            if (ingredientItemsForSavedState != null) {
                ingredientsSize = ingredientItemsForSavedState.size();
            }
        } else {
            ingredientsSize = ingredientItems.getCount();
        }

        String ingredientQuantity;
        String ingredientMeasure;
        String ingredientIngredient;

        LinearLayout ingredientsLayout = rootView.findViewById(R.id.recipe_ingredients_linear_layout);

        for (int i = 0; i < ingredientsSize; i++) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(rootView.getContext()).inflate(R.layout.layout_recipe_ingredients, null);
            view.setId(INGREDIENTS_ID + i);
            TextView ingredientsQuantityView = view.findViewById(R.id.layout_recipe_ingredients_quantity);
            TextView ingredientsMeasureView = view.findViewById(R.id.layout_recipe_ingredients_measure);
            TextView ingredientsIngredientView = view.findViewById(R.id.layout_recipe_ingredients_ingredient);

            if (savedInstanceState == null) {
                ingredientItems.moveToPosition(i);
                ingredientQuantity = ingredientItems.getString(ingredientItems.getColumnIndex(IngredientsEntry.INGREDIENTS_QUANTITY));
                ingredientMeasure = ingredientItems.getString(ingredientItems.getColumnIndex(IngredientsEntry.INGREDIENTS_MEASURE));
                ingredientIngredient = ingredientItems.getString(ingredientItems.getColumnIndex(IngredientsEntry.INGREDIENTS_INGREDIENT));
                ingredientItemsForSavedState.add(new IngredientItem(ingredientQuantity, ingredientMeasure, ingredientIngredient));
            } else {
                ingredientQuantity = ingredientItemsForSavedState.get(i).getIngredientQuantity();
                ingredientMeasure = ingredientItemsForSavedState.get(i).getIngredientMeasure();
                ingredientIngredient = ingredientItemsForSavedState.get(i).getIngredientIngredient();
            }

            ingredientsQuantityView.setText(ingredientQuantity);
            ingredientsMeasureView.setText(ingredientMeasure);
            ingredientsIngredientView.setText(ingredientIngredient);
            ingredientsLayout.addView(view);

        }
        return rootView;
    }

    public void setIngredientItems(Cursor ingredientItems) {
        this.ingredientItems = ingredientItems;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ON_SAVED_INSTANCE_INGREDIENT_ITEMS, ingredientItemsForSavedState);
    }
}
