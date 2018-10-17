package com.example.victor.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.objects.StepItem;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static int stepIndex = 0;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private ArrayList<StepItem> stepItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //Add video fragment
        DetailVideosFragment recipeVideosFragment = new DetailVideosFragment();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                stepItems = bundle.getParcelableArrayList(MainActivity.INTENT_STEP_ITEMS);
            }
            if (bundle != null && bundle.getInt(MainActivity.INTENT_STEP_ID) != 0) {
                stepIndex = bundle.getInt(MainActivity.INTENT_STEP_ID);
            }
        }
        if (stepItems != null) {
            recipeVideosFragment.setStepItems(stepItems);
            recipeVideosFragment.setStepIndex(stepIndex);
        } else {
            Log.e(LOG_TAG, "Cannot receive step items from intent");
        }

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_video_container, recipeVideosFragment)
                    .commit();
        }

        // Replace video fragment on click
        TextView previousStepButtonView = findViewById(R.id.activity_detail_previous_step);
        previousStepButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepIndex > 0) {
                    stepIndex--;
                    replaceStepItem(stepIndex, stepItems, fragmentManager);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_no_previous_step), Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView nextStepButtonView = findViewById(R.id.activity_detail_next_step);
        nextStepButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepIndex < stepItems.size() - 1) {
                    stepIndex++;
                    replaceStepItem(stepIndex, stepItems, fragmentManager);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_no_next_step), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void replaceStepItem(int stepIndex, ArrayList<StepItem> stepItems, FragmentManager fragmentManager) {
        DetailVideosFragment newRecipeVideosFragment = new DetailVideosFragment();
        newRecipeVideosFragment.setStepIndex(stepIndex);
        newRecipeVideosFragment.setStepItems(stepItems);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_video_container, newRecipeVideosFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }
}
