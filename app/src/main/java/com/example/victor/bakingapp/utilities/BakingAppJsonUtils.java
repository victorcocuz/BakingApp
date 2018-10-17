package com.example.victor.bakingapp.utilities;

import android.util.Log;

import com.example.victor.bakingapp.objects.IngredientItem;
import com.example.victor.bakingapp.objects.RecipeItem;
import com.example.victor.bakingapp.objects.StepItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/******
 * Created by Victor on 8/18/2018.
 ******/
public class BakingAppJsonUtils {

    private static final String LOG_TAG = BakingAppJsonUtils.class.getSimpleName();

    //Recipe fields
    private static final String BAKING_APP_RECIPE_ID = "id";
    private static final String BAKING_APP_RECIPE_NAME = "name";
    private static final String BAKING_APP_RECIPE_INGREDIENTS = "ingredients";
    private static final String BAKING_APP_RECIPE_STEPS = "steps";
    private static final String BAKING_APP_RECIPE_SERVING = "servings";

    //Ingredients fields
    private static final String BAKING_APP_INGREDIENTS_QUANTITY = "quantity";
    private static final String BAKING_APP_INGREDIENTS_MEASURE = "measure";
    private static final String BAKING_APP_INGREDIENTS_INGREDIENT = "ingredient";

    //Steps fields
    private static final String BAKING_APP_STEPS_ID = "id";
    private static final String BAKING_APP_STEPS_SHORT_DESCRIPTION = "shortDescription";
    private static final String BAKING_APP_STEPS_DESCRIPTION = "description";
    private static final String BAKING_APP_STEPS_VIDEO_URL = "videoURL";
    private static final String BAKING_APP_STEPS_THUMBNAIL_URL = "thumbnailURL";


    public static URL getURL(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Cannot create url from string", e);
        }
        return null;
    }

    public static String MakeHttpConnection(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(15000);
            httpUrlConnection.setReadTimeout(10000);
            httpUrlConnection.setRequestMethod("GET");

            if(httpUrlConnection.getResponseCode() == 200) {
                inputStream = httpUrlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Request Code error " + httpUrlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not retrieve JSON results ", e);
        } finally {
            if(httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
            return output.toString();
        }

        return null;
    }

    public static List<RecipeItem> extractDataFomJson (String jsonResponse) {
        List<RecipeItem> recipeItems = new ArrayList<>();

        int jsonId;
        String jsonName;
        String jsonServing;

        try {
            JSONArray jsonArrayRecipes = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArrayRecipes.length(); i++) {
                JSONObject jsonObjectRecipe = jsonArrayRecipes.optJSONObject(i);

                List<IngredientItem> jsonIngredientItems = new ArrayList<>();
                List<StepItem> jsonStepItems = new ArrayList<>();

                //ID, name and serving
                jsonId = jsonObjectRecipe.optInt(BAKING_APP_RECIPE_ID);
                jsonName = jsonObjectRecipe.optString(BAKING_APP_RECIPE_NAME);
                jsonServing = jsonObjectRecipe.optString(BAKING_APP_RECIPE_SERVING);

                //Ingredients
                JSONArray jsonArrayIngredients = jsonObjectRecipe.optJSONArray(BAKING_APP_RECIPE_INGREDIENTS);
                for (int j = 0; j < jsonArrayIngredients.length(); j++) {
                    JSONObject jsonObjectIngredient = jsonArrayIngredients.optJSONObject(j);

                    String jsonIngredientQuantity = jsonObjectIngredient.optString(BAKING_APP_INGREDIENTS_QUANTITY);
                    String jsonIngredientMeasure = jsonObjectIngredient.optString(BAKING_APP_INGREDIENTS_MEASURE);
                    String jsonIngredientIngredient = jsonObjectIngredient.optString(BAKING_APP_INGREDIENTS_INGREDIENT);

                    jsonIngredientItems.add(new IngredientItem(jsonIngredientQuantity, jsonIngredientMeasure, jsonIngredientIngredient));
                }

                //Steps
                JSONArray jsonArraySteps = jsonObjectRecipe.optJSONArray(BAKING_APP_RECIPE_STEPS);
                for (int k = 0; k < jsonArraySteps.length(); k++) {
                    JSONObject jsonObjectStep = jsonArraySteps.optJSONObject(k);

                    int jsonStepId = jsonObjectStep.optInt(BAKING_APP_STEPS_ID);
                    String jsonStepShortDescription = jsonObjectStep.optString(BAKING_APP_STEPS_SHORT_DESCRIPTION);
                    String jsonStepDescription = jsonObjectStep.optString(BAKING_APP_STEPS_DESCRIPTION);
                    String jsonStepVideoUrl = jsonObjectStep.optString(BAKING_APP_STEPS_VIDEO_URL);
                    String jsonStepThumbnailUrl = jsonObjectStep.optString(BAKING_APP_STEPS_THUMBNAIL_URL);

                    jsonStepItems.add(new StepItem(jsonStepId, jsonStepShortDescription, jsonStepDescription, jsonStepVideoUrl, jsonStepThumbnailUrl));
                }
                recipeItems.add(new RecipeItem(jsonId, jsonName, jsonIngredientItems, jsonStepItems, jsonServing));
            }
            return recipeItems;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
