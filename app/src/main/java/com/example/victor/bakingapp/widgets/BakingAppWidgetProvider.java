package com.example.victor.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.ui.DetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, boolean isRecipe, int recipeId) {
        RemoteViews remoteViews;
        if (isRecipe) {
            remoteViews = getRecipesRemoteView(context);
        } else {
            remoteViews = getStepsRemoteView(context, recipeId);
        }

        //Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, boolean isRecipe, int recipeId) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, isRecipe, recipeId);
        }
    }

    public static void updateStepWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, boolean isRecipe, int recipeId) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, isRecipe, recipeId);
        }
    }

    private static RemoteViews getRecipesRemoteView(Context context) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_baking_app);

        views.setInt(R.id.widget_baking_app, "setBackgroundResource", R.color.colorPrimaryDark);
        views.setViewVisibility(R.id.widget_baking_app_step_list_view, View.GONE);
        views.setViewVisibility(R.id.widget_baking_app_recipe_list_view, View.VISIBLE);

        // Set the ListWidgetServiceRecipes intent to act as the adapter for the ListView
        Intent intent = new Intent(context, ListWidgetServiceRecipes.class);
        intent.putExtra(BakingAppWidgetService.EXTRA_IS_RECIPE, true);
        intent.putExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, -1);
        views.setRemoteAdapter(R.id.widget_baking_app_recipe_list_view, intent);

        //Set the BakingAppWidgetService intent to launch when clicked
        Intent recipeStepIntent = new Intent(context, BakingAppWidgetService.class);
        recipeStepIntent.setAction(BakingAppWidgetService.ACTION_UPDATE_RECIPE_STEPS);
        PendingIntent servicePendingIntent = PendingIntent.getService(context, 0, recipeStepIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_baking_app_recipe_list_view, servicePendingIntent);

        return views;
    }

    private static RemoteViews getStepsRemoteView(Context context, int recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_baking_app);

        views.setInt(R.id.widget_baking_app, "setBackgroundResource", R.color.colorPrimaryDark);
        views.setViewVisibility(R.id.widget_baking_app_step_list_view, View.VISIBLE);
        views.setViewVisibility(R.id.widget_baking_app_recipe_list_view, View.GONE);

        Intent intent2 = new Intent(context, ListWidgetServiceSteps.class);
        intent2.putExtra(BakingAppWidgetService.EXTRA_IS_RECIPE, false);
        intent2.putExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, recipeId);
        views.setRemoteAdapter(R.id.widget_baking_app_step_list_view, intent2);

        Intent detailActivityIntent = new Intent(context, DetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, detailActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_baking_app_step_list_view, appPendingIntent);

        Intent backToRecipesIntent = new Intent(context, BakingAppWidgetService.class);
        backToRecipesIntent.setAction(BakingAppWidgetService.ACTION_UPDATE_RECIPES);
        PendingIntent servicePendingIntent = PendingIntent.getService(context, 0, backToRecipesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_baking_app_back_to_recipes, servicePendingIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BakingAppWidgetService.startActionUpdateRecipes(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

