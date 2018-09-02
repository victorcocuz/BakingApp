package com.example.victor.bakingapp.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.adapters.StepAdapter;
import com.example.victor.bakingapp.objects.StepItem;

import java.util.ArrayList;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeFragmentSteps extends Fragment implements StepAdapter.OnStepClickListener {
    private static final String LOG_TAG = RecipeFragmentSteps.class.getSimpleName();
    private static final int STEPS_ID = 2000;
    private ArrayList<StepItem> stepItems;
    private boolean twoPanes;
    RecyclerView recyclerView;
    StepAdapter stepAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

            recyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            stepAdapter = new StepAdapter(getContext(), stepItems, this, twoPanes);
            recyclerView.setAdapter(stepAdapter);
        return rootView;
    }

    public void setStepItems(ArrayList<StepItem> stepItems) {
        this.stepItems = stepItems;
    }

    public void setTwoPanes(boolean twoPanes) {
        this.twoPanes = twoPanes;
    }

    @Override
    public void OnStepClick(int stepIndex) {
        if (twoPanes) {
            FragmentManager fragmentManager = getFragmentManager();
            DetailVideosFragment newRecipeVideosFragment = new DetailVideosFragment();
            newRecipeVideosFragment.setStepIndex(stepIndex);
            newRecipeVideosFragment.setStepItems(stepItems);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_video_container_test, newRecipeVideosFragment)
                    .commit();
        }
    }
}