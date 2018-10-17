package com.example.victor.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.data.BakingContract;
import com.example.victor.bakingapp.objects.StepItem;
import com.example.victor.bakingapp.ui.MainActivity;

import java.util.ArrayList;

/******
 * Created by Victor on 10/14/2018.
 ******/
public class ListWidgetServiceSteps extends RemoteViewsService {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = ListWidgetServiceSteps.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent.getExtras() != null) {
            boolean isRecipe = intent.getExtras().getBoolean(BakingAppWidgetService.EXTRA_IS_RECIPE);
            int recipeId = intent.getExtras().getInt(BakingAppWidgetService.EXTRA_RECIPE_ID);
            return new ListRemoteViewsFactory2(this.getApplicationContext(), isRecipe, recipeId);
        }
        return null;
    }

    class ListRemoteViewsFactory2 implements RemoteViewsFactory {
        final Context context;
        Cursor cursor;
        final boolean isRecipe;
        final int recipeId;

        ListRemoteViewsFactory2(Context context, boolean isRecipe, int recipeId) {
            this.context = context;
            this.isRecipe = isRecipe;
            this.recipeId = recipeId;
        }

        @Override
        public void onCreate() {
        }

        //Called on start and when notifyAppWidgetViewDataChanged is called
        @Override
        public void onDataSetChanged() {
            if (cursor != null) cursor.close();

            if (isRecipe) {
                cursor = context.getContentResolver().query(
                        BakingContract.RecipesEntry.RECIPES_URI,
                        MainActivity.PROJECTION_RECIPE,
                        null,
                        null,
                        null);
            } else {
                String selection = BakingContract.StepsEntry.STEPS_RECIPE_ID + "=?";
                String[] selectionArgs = new String[]{String.valueOf(recipeId)};
                cursor = context.getContentResolver().query(
                        BakingContract.StepsEntry.STEPS_URI,
                        MainActivity.PROJECTION_STEPS,
                        selection,
                        selectionArgs,
                        null);
            }
        }

        @Override
        public void onDestroy() {
            cursor.close();
        }

        @Override
        public int getCount() {
            if (cursor == null) return 0;
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if (cursor == null || cursor.getCount() == 0) return null;
            cursor.moveToPosition(position);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);

            if (isRecipe) {
                String recipeName = cursor.getString(cursor.getColumnIndex(BakingContract.RecipesEntry.RECIPES_NAME));
                int recipeId = cursor.getInt(cursor.getColumnIndex(BakingContract.RecipesEntry.RECIPES_ID));

                views.setTextViewText(R.id.widget_recipe_text_view, recipeName);

                //Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
                Bundle extras = new Bundle();
                extras.putInt(MainActivity.INTENT_RECIPE_ID, recipeId);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                views.setOnClickFillInIntent(R.id.widget_recipe_text_view, fillInIntent);
            } else {
                String shortDescription = cursor.getString(cursor.getColumnIndex(BakingContract.StepsEntry.STEPS_SHORT_DESCRIPTION));

                views.setTextViewText(R.id.widget_recipe_text_view, shortDescription);

                ArrayList<StepItem> stepItems = new ArrayList<>();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    int stepId = cursor.getInt(cursor.getColumnIndex(BakingContract.StepsEntry.STEPS_RECIPE_ID));
                    String stepShortDescription = cursor.getString(cursor.getColumnIndex(BakingContract.StepsEntry.STEPS_SHORT_DESCRIPTION));
                    String stepDescription = cursor.getString(cursor.getColumnIndex(BakingContract.StepsEntry.STEPS_DESCRIPTION));
                    String stepVideoUrl = cursor.getString(cursor.getColumnIndex(BakingContract.StepsEntry.STEPS_VIDEO_URL));
                    String stepThumbnailUrl = cursor.getString(cursor.getColumnIndex(BakingContract.StepsEntry.STEPS_THUMBNAIL_URL));
                    stepItems.add(new StepItem(stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl));
                }

                Intent fillInIntent = new Intent();
                fillInIntent.putParcelableArrayListExtra(MainActivity.INTENT_STEP_ITEMS, stepItems);
                fillInIntent.putExtra(MainActivity.INTENT_STEP_ID, position);
                views.setOnClickFillInIntent(R.id.widget_recipe_text_view, fillInIntent);
            }

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            //Treat all items in the GridView the same
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
