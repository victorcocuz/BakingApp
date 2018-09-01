package com.example.victor.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.resources.Assets;
import com.example.victor.bakingapp.ui.MainActivity;
import com.example.victor.bakingapp.ui.RecipeActivity;

import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;

/******
 * Created by Victor on 8/18/2018.
 ******/
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();
    private Cursor recipesCursor;
    private Context context;

    public RecipeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        holder.recipeImageView.setImageResource(Assets.getCakeImages().get(position));
        recipesCursor.moveToPosition(position);
        final int recipeId = recipesCursor.getInt(recipesCursor.getColumnIndex(RecipesEntry.RECIPES_ID));
        final String recipeName = recipesCursor.getString(recipesCursor.getColumnIndex(RecipesEntry.RECIPES_NAME));

        holder.recipeTextView.setText(recipeName);
        holder.recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRecipeActivity = new Intent(context, RecipeActivity.class);
                goToRecipeActivity.putExtra(MainActivity.INTENT_RECIPE_ID, recipeId);
                goToRecipeActivity.putExtra(MainActivity.INTENT_RECIPE_NAME, recipeName);
                context.startActivity(goToRecipeActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesCursor.getCount();
    }

    public void updateRecipes(Cursor recipeItems) {
        this.recipesCursor = recipeItems;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImageView;
        private TextView recipeTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.card_recipe_image_view);
            recipeTextView = itemView.findViewById(R.id.card_recipe_text_view);
        }
    }
}
