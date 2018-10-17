package com.example.victor.bakingapp.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.adapters.RecipeAdapter;
import com.example.victor.bakingapp.data.BakingContract;

import java.util.Objects;

/******
 * Created by Victor on 8/16/2018.
 ******/
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private RecipeAdapter recipeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_list, container, false);

        RecyclerView recipesRecyclerView = rootView.findViewById(R.id.fragment_recipes_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesRecyclerView.setHasFixedSize(true);
        recipeAdapter = new RecipeAdapter(getContext());
        recipesRecyclerView.setAdapter(recipeAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).getLoaderManager().initLoader(MainActivity.CURSOR_RECIPE_LOADER_ID, null, this);
        }
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                BakingContract.RecipesEntry.RECIPES_URI,
                MainActivity.PROJECTION_RECIPE,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recipeAdapter.updateRecipes(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
