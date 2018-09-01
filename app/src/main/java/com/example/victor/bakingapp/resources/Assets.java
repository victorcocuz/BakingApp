package com.example.victor.bakingapp.resources;

import com.example.victor.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

/******
 * Created by Victor on 8/16/2018.
 ******/
public class Assets {

    public static final List cakeImages = new ArrayList<Integer>() {{
        add(R.drawable.image_nutella_pie);
        add(R.drawable.image_brownies);
        add(R.drawable.image_yellow_cake);
        add(R.drawable.image_cheese_cake);
    }};

    public static final List cakeNames = new ArrayList<Integer>() {{
        add(R.string.list_nutella_pie);
        add(R.string.list_brownies);
        add(R.string.list_yellow_cake);
        add(R.string.list_cheese_cake);
    }};

    public static List<Integer> getCakeImages() {
        return cakeImages;
    }

    public static List<Integer> getCakeNames() {return cakeNames; }
}
