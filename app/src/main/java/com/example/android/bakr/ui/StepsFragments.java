package com.example.android.bakr.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Step;
import com.example.android.bakr.model.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conde on 5/14/2018.
 */

public class StepsFragments extends android.support.v4.app.Fragment implements StepsRecyclerViewAdapter.StepsOnclickCallBack{
    DishClick mOnDishClick;
    private static String dishName;
    private List<Step> mSteps;
    public static final String DISH_NAME_INDEX = "dish_name";
    public static final String STEPS_INDEX = "steps_index";
    public StepsFragments() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnDishClick = (DishClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement mOnDishClick");
        }
    }


    public interface DishClick {
        void onDishClick(int position, List<Step> steps);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            dishName = savedInstanceState.getString(DISH_NAME_INDEX);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        mSteps = Utils.getSteps(getContext(), Utils.loadJSONFromAsset(getContext()), dishName);
        StepsRecyclerViewAdapter stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(
                mSteps
                , getContext(), this);
        RecyclerView stepsRecyclerView = rootView.findViewById(R.id.rv_steps);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(getContext());
        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
        stepsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepsRecyclerView.setAdapter(stepsRecyclerViewAdapter);
        return rootView;
    }

    //Helper method used by the parent Activity to change the dishName.
    public void setDishName(String dishName) {
        StepsFragments.dishName = dishName;
    }

    //*Communication interface with the recyclerView Adapter
    // The information are sent to the DishActivity which will then send it to the following
    // Activity to display the videos and description*/
    @Override
    public void onClickCallback(int position, List<Step> steps) {
        mOnDishClick.onDishClick(position, steps);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DISH_NAME_INDEX, dishName);
        outState.putParcelableArrayList(STEPS_INDEX, (ArrayList<? extends Parcelable>) mSteps);
    }

    //This is accessed in DishActivity to automatically play video on tablets
    public List<Step> getSteps() {
        return mSteps;
    }
}


