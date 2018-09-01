package com.example.victor.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.victor.bakingapp.R;

/******
 * Created by Victor on 8/19/2018.
 ******/
public class TODELETERecipeFragmentTitle extends Fragment {

    private static final String LOG_TAG = TODELETERecipeFragmentTitle.class.getSimpleName();


    View rootView;
    int recipeId = 0;

//    OnStartCookingClickListener onStartCookingClickListener;
//
//    public interface OnStartCookingClickListener {
//        void onButtonSelected(int recipeId, Cursor data);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            onStartCookingClickListener = (OnStartCookingClickListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement OnImageClickListener");
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_like, container, false);
        return rootView;
    }
}
