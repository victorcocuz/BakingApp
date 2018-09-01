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
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeFragmentSteps extends Fragment {
    private static final String LOG_TAG = RecipeFragmentSteps.class.getSimpleName();
    private static final int STEPS_ID = 2000;
    private Cursor stepItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        LinearLayout stepsLayout = rootView.findViewById(R.id.recipe_steps_linear_layout);
        for (int i = 0; i < stepItems.getCount(); i++) {
            stepItems.moveToPosition(i);

            //Populate steps as linear layout in master fragment
            @SuppressLint("InflateParams") View view = LayoutInflater.from(rootView.getContext()).inflate(R.layout.layout_recipe_steps, null);
            view.setId(STEPS_ID + i);
            TextView stepsTitleView = view.findViewById(R.id.layout_recipe_steps_title);
            TextView stepsNumberView = view.findViewById(R.id.layout_recipe_steps_number);
            TextView stepsShortDescriptionView = view.findViewById(R.id.layout_recipe_steps_short_description);
            TextView stepsDescriptionView = view.findViewById(R.id.layout_recipe_steps_description);
            stepsTitleView.setText(rootView.getResources().getString(R.string.recipe_individual_step));
            stepsNumberView.setText(String.valueOf(i + 1));
            String stepsShortDescription = " - " + stepItems.getString(stepItems.getColumnIndex(StepsEntry.STEPS_SHORT_DESCRIPTION));
            stepsShortDescriptionView.setText(stepsShortDescription);
            if (i > 0) {
                stepsDescriptionView.setText(stepItems.getString(stepItems.getColumnIndex(StepsEntry.STEPS_DESCRIPTION)));
            }
            stepsLayout.addView(view);
        }
        return rootView;
    }

    public void setStepItems(Cursor stepItems) {
        this.stepItems = stepItems;
    }
}