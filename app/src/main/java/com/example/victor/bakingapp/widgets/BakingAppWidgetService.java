package com.example.victor.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.victor.bakingapp.R;

/******
 * Created by Victor on 10/7/2018.
 ******/
public class BakingAppWidgetService extends IntentService {

    public static final String ACTION_UPDATE_RECIPES = "com.example.victor.bakingapp.action.update_recipes";
    public static final String ACTION_UPDATE_RECIPE_STEPS = "com.example.victor.bakingapp.action.update_recipe_steps";
    public static final String EXTRA_IS_RECIPE = "BakingAppWidgetIsRecipe";
    public static final String EXTRA_RECIPE_ID = "BakingAppWidgetServiceId";
    private static final String LOG_TAG = BakingAppWidgetService.class.getSimpleName();

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionUpdateRecipes(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPES);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (ACTION_UPDATE_RECIPES.equals(intent.getAction())) {
                handleActionUpdateRecipes();
            } else if (ACTION_UPDATE_RECIPE_STEPS.equals(intent.getAction())) {
                int recipeId = 0;
                if (intent.getExtras() != null) {
                    recipeId = intent.getExtras().getInt(EXTRA_RECIPE_ID);
                }
                handleActionUpdateRecipeSteps(recipeId);
            }
        }
    }

    private void handleActionUpdateRecipes() {
        int recipeId = -1;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));

        //Trigger data update to handle the GridView Widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_baking_app_recipe_list_view);

        //Update all widgets
        BakingAppWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, true, recipeId);
    }

    private void handleActionUpdateRecipeSteps(int recipeId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));

        //Trigger data update to handle the GridView Widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_baking_app_step_list_view);

        //Update all widgets
        BakingAppWidgetProvider.updateStepWidgets(this, appWidgetManager, appWidgetIds, false, recipeId);

    }

}
