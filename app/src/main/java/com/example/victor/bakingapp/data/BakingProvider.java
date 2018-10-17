package com.example.victor.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.victor.bakingapp.data.BakingContract.IngredientsEntry;
import com.example.victor.bakingapp.data.BakingContract.RecipesEntry;
import com.example.victor.bakingapp.data.BakingContract.StepsEntry;

/******
 * Created by Victor on 8/21/2018.
 ******/
public class BakingProvider extends ContentProvider {

    private static final int CODE_RECIPES = 100;
    private static final int CODE_RECIPES_ITEM = 101;
    private static final int CODE_INGREDIENTS = 200;
    private static final int CODE_INGREDIENTS_ITEM = 201;
    private static final int CODE_STEPS = 300;
    private static final int CODE_STEPS_ITEM = 301;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final String LOG_TAG = BakingProvider.class.getSimpleName();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BakingContract.BAKING_AUTHORITY, BakingContract.BAKING_PATH_RECIPES, CODE_RECIPES);
        uriMatcher.addURI(BakingContract.BAKING_AUTHORITY, BakingContract.BAKING_PATH_RECIPES + "#", CODE_RECIPES_ITEM);
        uriMatcher.addURI(BakingContract.BAKING_AUTHORITY, BakingContract.BAKING_PATH_INGREDIENTS, CODE_INGREDIENTS);
        uriMatcher.addURI(BakingContract.BAKING_AUTHORITY, BakingContract.BAKING_PATH_INGREDIENTS + "#", CODE_INGREDIENTS_ITEM);
        uriMatcher.addURI(BakingContract.BAKING_AUTHORITY, BakingContract.BAKING_PATH_STEPS, CODE_STEPS);
        uriMatcher.addURI(BakingContract.BAKING_AUTHORITY, BakingContract.BAKING_PATH_STEPS + "#", CODE_STEPS_ITEM);
        return uriMatcher;
    }

    private BakingDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new BakingDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE_RECIPES:
                cursor = database.query(RecipesEntry.RECIPES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_RECIPES_ITEM:
                selection = RecipesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(RecipesEntry.RECIPES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_INGREDIENTS:
                cursor = database.query(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_INGREDIENTS_ITEM:
                selection = IngredientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_STEPS:
                cursor = database.query(StepsEntry.STEPS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_STEPS_ITEM:
                selection = StepsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StepsEntry.STEPS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id;

        if (values == null || values.size() == 0) {
            return null;
        }

        switch (uriMatcher.match(uri)) {
                case CODE_RECIPES:
                id = database.insert(RecipesEntry.RECIPES_TABLE_NAME,
                        null,
                        values);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                }
                break;
            case CODE_INGREDIENTS:
                id = database.insert(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        null,
                        values);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                }
                break;
            case CODE_STEPS:
                id = database.insert(StepsEntry.STEPS_TABLE_NAME,
                        null,
                        values);
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                }
                break;

            default:
                throw new IllegalArgumentException("Insert is not supported for " + uri);
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated;

        if (values == null || values.size() == 0) {
            return 0;
        }

        switch (uriMatcher.match(uri)) {
            case CODE_RECIPES:
                rowsUpdated = database.update(RecipesEntry.RECIPES_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_RECIPES_ITEM:
                selection = RecipesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(RecipesEntry.RECIPES_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_INGREDIENTS:
                rowsUpdated = database.update(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_INGREDIENTS_ITEM:
                selection = IngredientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_STEPS:
                rowsUpdated = database.update(StepsEntry.STEPS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_STEPS_ITEM:
                selection = RecipesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(StepsEntry.STEPS_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

//        if (rowsUpdated != 0 && getContext() != null) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }

        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case CODE_RECIPES:
                rowsDeleted = database.delete(RecipesEntry.RECIPES_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_RECIPES_ITEM:
                selection = RecipesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(RecipesEntry.RECIPES_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_INGREDIENTS:
                rowsDeleted = database.delete(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_INGREDIENTS_ITEM:
                selection = IngredientsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(IngredientsEntry.INGREDIENTS_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_STEPS:
                rowsDeleted = database.delete(StepsEntry.STEPS_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_STEPS_ITEM:
                selection = StepsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StepsEntry.STEPS_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

        if (rowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CODE_RECIPES:
                return BakingContract.RECIPES_CONTENT_DIR_BASE;
            case CODE_RECIPES_ITEM:
                return BakingContract.RECIPES_CONTENT_ITEM_BASE;
            case CODE_INGREDIENTS:
                return BakingContract.INGREDIENTS_CONTENT_DIR_BASE;
            case CODE_INGREDIENTS_ITEM:
                return BakingContract.INGREDIENTS_CONTENT_ITEM_BASE;
            case CODE_STEPS:
                return BakingContract.STEPS_CONTENT_DIR_BASE;
            case CODE_STEPS_ITEM:
                return BakingContract.STEPS_CONTENT_ITEM_BASE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + uri);
        }
    }

    @Override
    public void shutdown() {
        dbHelper.close();
        super.shutdown();
    }
}
