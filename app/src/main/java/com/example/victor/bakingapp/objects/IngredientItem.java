package com.example.victor.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/******
 * Created by Victor on 8/18/2018.
 ******/
public class IngredientItem implements Parcelable {

    public static final Creator<IngredientItem> CREATOR = new Creator<IngredientItem>() {
        @Override
        public IngredientItem createFromParcel(Parcel in) {
            return new IngredientItem(in);
        }

        @Override
        public IngredientItem[] newArray(int size) {
            return new IngredientItem[size];
        }
    };

    private String ingredientQuantity;
    private String ingredientMeasure;
    private String ingredientIngredient;

    public IngredientItem(String ingredientQuantity, String ingredientMeasure, String ingredientIngredient) {
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientIngredient = ingredientIngredient;
    }

    public String getIngredientQuantity() {
        return ingredientQuantity;
    }
    public String getIngredientMeasure() {
        return ingredientMeasure;
    }
    public String getIngredientIngredient() {
        return ingredientIngredient;
    }

    protected IngredientItem(Parcel in) {
        ingredientQuantity = in.readString();
        ingredientMeasure = in.readString();
        ingredientIngredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredientQuantity);
        dest.writeString(ingredientMeasure);
        dest.writeString(ingredientIngredient);
    }
}
