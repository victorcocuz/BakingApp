package com.example.victor.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.objects.StepItem;

import com.example.victor.bakingapp.data.BakingContract.StepsEntry;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeMasterFragment.OnStartCookingClickListener {

    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    static int recipeId = 0;
    ArrayList<StepItem> stepItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        FragmentManager fragmentManager = getSupportFragmentManager();

        //Inflate master fragment
        RecipeMasterFragment masterRecipeFragment = new RecipeMasterFragment();
        masterRecipeFragment.setArguments(getIntent().getExtras());
        fragmentManager.beginTransaction()
                .add(R.id.fragment_master_recipe_container, masterRecipeFragment)
                .commit();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            recipeId = bundle.getInt(MainActivity.INTENT_RECIPE_ID);
        }
    }

    @Override
    public void onButtonSelected(int recipeId, Cursor data) {
        stepItems = new ArrayList<>();
        //Populate an arraylist to pass on click
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            int stepId = data.getInt(data.getColumnIndex(StepsEntry.STEPS_ID));
            String stepShortDescription = data.getString(data.getColumnIndex(StepsEntry.STEPS_SHORT_DESCRIPTION));
            String stepDescription = data.getString(data.getColumnIndex(StepsEntry.STEPS_DESCRIPTION));
            String stepVideoUrl = data.getString(data.getColumnIndex(StepsEntry.STEPS_VIDEO_URL));
            String stepThumbnailUrl = data.getString(data.getColumnIndex(StepsEntry.STEPS_THUMBNAIL_URL));
            stepItems.add(new StepItem(stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl));
        }

        Intent goToRecipeCookingActivity = new Intent(this, RecipeDetailActivity.class);
        goToRecipeCookingActivity.putExtra(MainActivity.INTENT_RECIPE_ID, recipeId);
        goToRecipeCookingActivity.putParcelableArrayListExtra(MainActivity.INTENT_STEP_ITEMS, stepItems);
        startActivity(goToRecipeCookingActivity);
    }
}
