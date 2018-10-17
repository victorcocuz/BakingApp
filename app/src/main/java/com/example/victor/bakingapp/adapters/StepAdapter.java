package com.example.victor.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.objects.StepItem;

import java.util.ArrayList;

/******
 * Created by Victor on 9/2/2018.
 ******/
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();
    private ArrayList<StepItem> stepItems;
    private Context context;
    private OnStepClickListener onStepClickHandler;
    private boolean twoPanes;

    public StepAdapter(Context context, ArrayList<StepItem> stepItems, OnStepClickListener onStepClickHandler, boolean twoPanes) {
        this.context = context;
        this.stepItems = stepItems;
        this.onStepClickHandler = onStepClickHandler;
        this.twoPanes = twoPanes;
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepViewHolder holder, final int position) {
        String stepsShortDescription = stepItems.get(position).getStepShortDescription();
        String stepsDescription = stepItems.get(position).getStepDescription();

        holder.stepsTitleView.setText(context.getResources().getString(R.string.recipe_individual_step));
        holder.stepsNumberView.setText(String.valueOf(position));
        holder.stepsShortDescriptionView.setText(String.format(" - %s", stepsShortDescription));

        if (position > 0 && !twoPanes) {
            holder.stepsDescriptionView.setText(stepsDescription);
        }  else {
            holder.stepsDescriptionView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stepItems.size();
    }

    public interface OnStepClickListener {
        void OnStepClick(int stepIndex);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView stepsTitleView;
        private TextView stepsNumberView;
        private TextView stepsShortDescriptionView;
        private TextView stepsDescriptionView;

        StepViewHolder(View itemView) {
            super(itemView);
            stepsTitleView = itemView.findViewById(R.id.layout_recipe_steps_title);
            stepsNumberView = itemView.findViewById(R.id.layout_recipe_steps_number);
            stepsShortDescriptionView = itemView.findViewById(R.id.layout_recipe_steps_short_description);
            stepsDescriptionView = itemView.findViewById(R.id.layout_recipe_steps_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int stepIndex = getAdapterPosition();
            onStepClickHandler.OnStepClick(stepIndex);
        }
    }
}
