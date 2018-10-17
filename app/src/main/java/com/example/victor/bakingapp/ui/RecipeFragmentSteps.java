package com.example.victor.bakingapp.ui;

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
    @SuppressWarnings("unused")
    private static final String LOG_TAG = RecipeFragmentSteps.class.getSimpleName();
    private static final String ON_SAVED_INSTANCE_TWO_PANES = "savedStateTwoPanes";
    private static final String ON_SAVED_INSTANCE_STEP_ITEMS = "savedStateStepItems";
    private Bundle mSavedInstanceState;
    private ArrayList<StepItem> stepItems;
    private boolean twoPanes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        mSavedInstanceState = savedInstanceState;

        if (savedInstanceState != null) {
            twoPanes = savedInstanceState.getBoolean(ON_SAVED_INSTANCE_TWO_PANES);
            stepItems = savedInstanceState.getParcelableArrayList(ON_SAVED_INSTANCE_TWO_PANES);
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        StepAdapter stepAdapter = new StepAdapter(getContext(), stepItems, this, twoPanes);
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
            if (mSavedInstanceState == null) {
                FragmentManager fragmentManager = getFragmentManager();
                DetailVideosFragment newRecipeVideosFragment = new DetailVideosFragment();
                newRecipeVideosFragment.setStepIndex(stepIndex);
                newRecipeVideosFragment.setStepItems(stepItems);
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_video_container_test, newRecipeVideosFragment)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ON_SAVED_INSTANCE_TWO_PANES, twoPanes);
        outState.putParcelableArrayList(ON_SAVED_INSTANCE_STEP_ITEMS, stepItems);
    }
}