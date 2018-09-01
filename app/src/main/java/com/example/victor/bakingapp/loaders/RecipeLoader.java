package com.example.victor.bakingapp.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.victor.bakingapp.objects.RecipeItem;
import com.example.victor.bakingapp.utilities.BakingAppJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/******
 * Created by Victor on 8/18/2018.
 ******/
public class RecipeLoader extends AsyncTaskLoader<List<RecipeItem>> {

    private static final String LOG_TAG = RecipeLoader.class.getSimpleName();
    private static final String BAKING_APP_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<RecipeItem> loadInBackground() {
        URL url = BakingAppJsonUtils.getURL(BAKING_APP_URL);
        String jsonResponse;
        try {
            jsonResponse = BakingAppJsonUtils.MakeHttpConnection(url);
            return BakingAppJsonUtils.extractDataFomJson(jsonResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not make Http Request", e);
        }

        return null;
    }
}
