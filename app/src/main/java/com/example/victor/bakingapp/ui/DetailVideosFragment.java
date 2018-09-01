package com.example.victor.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.objects.StepItem;

import java.util.ArrayList;

/******
 * Created by Victor on 8/27/2018.
 ******/
public class DetailVideosFragment extends Fragment {

    private static final String LOG_TAG = DetailVideosFragment.class.getSimpleName();
    View rootView;
    static int stepIndex;
    static ArrayList<StepItem> stepItems;

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
        if (stepItem.getStepVideoUrl() != null && stepItem.getStepVideoUrl().length() > 0) {
            video = stepItem.getStepVideoUrl();
        } else if (stepItem.getStepThumbnailUrl() != null && stepItem.getStepThumbnailUrl().length() > 0) {
            video = stepItem.getStepThumbnailUrl();
        } else {
            video = rootView.getResources().getString(R.string.no_visual_information_available);
        }
        detailVideoView.setText(video);

        //Set description
        String description;
        if (stepItem.getStepDescription() != null && stepItem.getStepDescription().length() > 0) {
            description = stepItem.getStepDescription();
        } else {
            description = rootView.getResources().getString(R.string.no_description_available);
        }
        detailDescriptionView.setText(description);
    }

    public void setStepIndex(int stepIndex) {
        DetailVideosFragment.stepIndex = stepIndex;
    }

    public void setStepItems(ArrayList<StepItem> stepItems) {
        DetailVideosFragment.stepItems = stepItems;
    }
}
