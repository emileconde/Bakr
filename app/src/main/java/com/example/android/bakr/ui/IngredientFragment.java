package com.example.android.bakr.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Ingredient;
import com.example.android.bakr.model.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conde on 5/14/2018.
 */

public class IngredientFragment extends Fragment{

    private static String dishName;
    private List<Ingredient> mIngredients;
    public static final String DISH_INDEX = "dish_index";
    public static final String INGREDIENT_LIST_INDEX = "ingredient_list_index";
    IngredientsReady mIngredientsReady;
    public IngredientFragment() {
    }

    //Lets the calling activity know when the ingredients are ready.
    // Its purpose to send to the list of ingredient over to 'DishActivity' which
    // will trigger the intent service to display the list in the widget./
    public interface IngredientsReady{
        void onIngredientsReady(List<Ingredient> ingredients);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mIngredientsReady = (IngredientsReady) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement IngredientsReady");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            dishName = savedInstanceState.getString(DISH_INDEX);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENT_LIST_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_ingedient, container, false);
        mIngredients =  Utils.getIngredients(getContext(), Utils.loadJSONFromAsset(getContext()),
                dishName);
        mIngredientsReady.onIngredientsReady(mIngredients);
        IngredientRecyclerViewAdapter ingredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(mIngredients,
                getContext());
        RecyclerView ingredientRecyclerView = rootView.findViewById(R.id.rv_ingredients);
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getContext());
        ingredientLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientRecyclerView.setAdapter(ingredientRecyclerViewAdapter);

        return rootView;
    }

    public void setDishName(String dishName) {
        IngredientFragment.dishName = dishName;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DISH_INDEX, dishName);
        outState.putParcelableArrayList(INGREDIENT_LIST_INDEX, (ArrayList<? extends Parcelable>) mIngredients);
    }
}
