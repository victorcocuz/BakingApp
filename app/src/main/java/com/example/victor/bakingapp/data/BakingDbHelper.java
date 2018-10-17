package com.example.victor.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;

/******
 * Created by Victor on 8/21/2018.
 ******/
public class BakingDbHelper extends SQLiteOpenHelper {

    private static final String BAKING_DB_NAME = "baking.db";
    private static final int BAKING_DB_VERSION = 1;

    public BakingDbHelper(Context context) {
        super(context, BAKING_DB_NAME, null, BAKING_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE_RECIPES = "CREATE TABLE " + RecipesEntry.RECIPES_TABLE_NAME + " ("
                + RecipesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecipesEntry.RECIPES_ID + " INTEGER, "
                + RecipesEntry.RECIPES_NAME + " TEXT, "
                + RecipesEntry.RECIPES_SERVING + " TEXT, "
                + RecipesEntry.RECIPES_IMAGE + " TEXT);";
        db.execSQL(SQL_CREATE_TABLE_RECIPES);

        final String SQL_CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + IngredientsEntry.INGREDIENTS_TABLE_NAME + " ("
                + IngredientsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IngredientsEntry.INGREDIENTS_RECIPE_ID + " INTEGER, "
                + IngredientsEntry.INGREDIENTS_ID + " INTEGER, "
                + IngredientsEntry.INGREDIENTS_REAL_QUANTITY + " TEXT, "
                + IngredientsEntry.INGREDIENTS_QUANTITY + " TEXT, "
                + IngredientsEntry.INGREDIENTS_MEASURE + " TEXT, "
                + IngredientsEntry.INGREDIENTS_INGREDIENT + " TEXT);";
        db.execSQL(SQL_CREATE_TABLE_INGREDIENTS);

        final String SQL_CREATE_TABLE_STEPS = "CREATE TABLE " + StepsEntry.STEPS_TABLE_NAME + " ("
                + StepsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StepsEntry.STEPS_RECIPE_ID + " INTEGER, "
                + StepsEntry.STEPS_ID + " INTEGER, "
                + StepsEntry.STEPS_SHORT_DESCRIPTION + " TEXT, "
                + StepsEntry.STEPS_DESCRIPTION + " TEXT, "
                + StepsEntry.STEPS_VIDEO_URL + " TEXT, "
                + StepsEntry.STEPS_THUMBNAIL_URL + " TEXT);";
        db.execSQL(SQL_CREATE_TABLE_STEPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipesEntry.RECIPES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientsEntry.INGREDIENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepsEntry.STEPS_TABLE_NAME);
        onCreate(db);
    }
}