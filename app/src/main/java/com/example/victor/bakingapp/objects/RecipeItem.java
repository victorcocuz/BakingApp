package com.example.victor.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/******
 * Created by Victor on 8/18/2018.
 ******/
public class RecipeItem implements Parcelable {

    public static final Creator<RecipeItem> CREATOR = new Creator<RecipeItem>() {
        @Override
        public RecipeItem createFromParcel(Parcel in) {
            return new RecipeItem(in);
        }

        @Override
        public RecipeItem[] newArray(int size) {
            return new RecipeItem[size];
        }
    };

    private final int recipeId;
    private final String recipeName;
    private final List<IngredientItem> recipeIngredientItems;
    private final List<StepItem> recipeStepItems;
    private final String recipeServing;

    public RecipeItem(int recipeId, String recipeName, List<IngredientItem> recipeIngredientItems, List<StepItem> recipeStepItems, String recipeServing) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeIngredientItems = recipeIngredientItems;
        this.recipeStepItems = recipeStepItems;
        this.recipeServing = recipeServing;
    }

    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public List<IngredientItem> getRecipeIngredientItems() { return recipeIngredientItems; }
    public List<StepItem> getRecipeStepItems() { return recipeStepItems; }
    public String getRecipeServing() { return recipeServing; }

    @SuppressWarnings("unchecked")
    private RecipeItem(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        recipeIngredientItems = in.readArrayList(IngredientItem.class.getClassLoader());
        recipeStepItems = in.readArrayList(StepItem.class.getClassLoader());
        recipeServing = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(recipeName);
        dest.writeList(recipeIngredientItems);
        dest.writeList(recipeStepItems);
        dest.writeString(recipeServing);
    }
}
