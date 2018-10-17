package com.example.victor.bakingapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/******
 * Created by Victor on 8/21/2018.
 ******/
public class BakingContract {

    public static final String BAKING_SCHEME = "content://";
    public static final String BAKING_AUTHORITY = "com.example.victor.bakingapp";
    public static final Uri BAKING_BASE_URI = Uri.parse(BAKING_SCHEME + BAKING_AUTHORITY);

    public static final String BAKING_PATH_RECIPES = "recipes";
    public static final String BAKING_PATH_INGREDIENTS = "ingredients";
    public static final String BAKING_PATH_STEPS = "steps";

    public static final String RECIPES_CONTENT_DIR_BASE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BAKING_AUTHORITY + "/" + BAKING_PATH_RECIPES;
    public static final String RECIPES_CONTENT_ITEM_BASE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BAKING_AUTHORITY + "/" + BAKING_PATH_RECIPES;
    public static final String INGREDIENTS_CONTENT_DIR_BASE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BAKING_AUTHORITY + "/" + BAKING_PATH_INGREDIENTS;
    public static final String INGREDIENTS_CONTENT_ITEM_BASE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BAKING_AUTHORITY + "/" + BAKING_PATH_INGREDIENTS;
    public static final String STEPS_CONTENT_DIR_BASE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BAKING_AUTHORITY + "/" + BAKING_PATH_STEPS;
    public static final String STEPS_CONTENT_ITEM_BASE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BAKING_AUTHORITY + "/" + BAKING_PATH_STEPS;

    public static final class RecipesEntry implements BaseColumns {
        public static final Uri RECIPES_URI = BAKING_BASE_URI.buildUpon()
                .appendPath(BAKING_PATH_RECIPES)
                .build();

        public static final String RECIPES_TABLE_NAME = "recipesTable";
        public static final String RECIPES_ID = "recipesId";
        public static final String RECIPES_NAME = "recipesName";
        public static final String RECIPES_SERVING = "recipesServing";
        public static final String RECIPES_IMAGE = "recipesImage";
    }

    public static final class IngredientsEntry implements BaseColumns {
        public static final Uri INGREDIENTS_URI = BAKING_BASE_URI.buildUpon()
                .appendPath(BAKING_PATH_INGREDIENTS)
                .build();

        public static final String INGREDIENTS_TABLE_NAME = "ingredientsTable";
        public static final String INGREDIENTS_RECIPE_ID = "ingredientsRecipeId";
        public static final String INGREDIENTS_ID = "ingredientsId";
        public static final String INGREDIENTS_REAL_QUANTITY = "ingredientsRealQuantity";
        public static final String INGREDIENTS_QUANTITY = "ingredientsQuantity";
        public static final String INGREDIENTS_MEASURE = "ingredientsMeasure";
        public static final String INGREDIENTS_INGREDIENT = "ingredientsIngredient";
    }

    public static final class StepsEntry implements BaseColumns {
        public static final Uri STEPS_URI = BAKING_BASE_URI.buildUpon()
                .appendPath(BAKING_PATH_STEPS)
                .build();

        public static final String STEPS_TABLE_NAME = "stepsTable";
        public static final String STEPS_RECIPE_ID = "stepsRecipeId";
        public static final String STEPS_ID = "stepsId";
        public static final String STEPS_SHORT_DESCRIPTION = "stepsShortDescription";
        public static final String STEPS_DESCRIPTION = "stepsDescription";
        public static final String STEPS_VIDEO_URL = "stepsVideoUrl";
        public static final String STEPS_THUMBNAIL_URL = "stepsThumbnailUrl";

    }
}
