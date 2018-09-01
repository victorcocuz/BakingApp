package com.example.victor.bakingapp.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;
import com.example.victor.bakingapp.objects.StepItem;

import java.util.ArrayList;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class RecipeVideosFragment extends Fragment {

    private static final String LOG_TAG = RecipeVideosFragment.class.getSimpleName();
    View rootView;
    static int stepIndex;
    static ArrayList<StepItem> stepItems;

    public interface RecipeOnClickListener {
        public void onRecipeClicked(int position);
    }

    RecipeOnClickListener recipeOnClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        populateDetailFragment(stepItems, stepIndex);
        return rootView;
    }

    public void populateDetailFragment(ArrayList<StepItem> stepItems, int stepIndex) {
        TextView detailVideoView = rootView.findViewById(R.id.fragment_recipe_detail_videos);
        TextView detailDescriptionView = rootView.findViewById(R.id.fragment_recipe_detail_description);

        StepItem stepItem = stepItems.get(stepIndex);

        //Set video
        String video;
        if (stepItem.getStepVideoUrl() != null) {
            video = stepItem.getStepVideoUrl();
        } else if (stepItem.getStepThumbnailUrl() != null) {
            video = stepItem.getStepThumbnailUrl();
        } else {
            video = rootView.getResources().getString(R.string.no_visual_information_available);
        }

        //Set description
        String description;
        if (stepItem.getStepDescription() != null) {
            description = stepItem.getStepDescription();
        } else {
            description = rootView.getResources().getString(R.string.no_description_available);
        }

        detailVideoView.setText(video);
        detailDescriptionView.setText(description);
    }

    public void setStepIndex(int stepIndex) {
        RecipeVideosFragment.stepIndex = stepIndex;
    }

    public void setStepItems(ArrayList<StepItem> stepItems) {
        RecipeVideosFragment.stepItems = stepItems;
    }
}
