package com.example.victor.bakingapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.objects.RecipeItem;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeFragmentServes extends Fragment {
    private static final String LOG_TAG = RecipeFragmentServes.class.getSimpleName();
    private int recipeServings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_serves, container, false);

        TextView recipeServeView = rootView.findViewById(R.id.recipe_serves_text);
        recipeServeView.setText(String.valueOf(recipeServings));

        return rootView;
    }

    public void setRecipeItem(int recipeServings) {
        this.recipeServings = recipeServings;
    }
}
