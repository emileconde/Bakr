package com.example.android.bakr.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Dish;
import com.example.android.bakr.model.Utils;

import java.util.List;

/**
 * Created by conde on 5/11/2018.
 */

public class MasterListFragment extends Fragment implements DishRecyclerViewAdapter.ListItemClickListener{

    public interface MasterListFragmentClicked {
        void onclickCallback(int position, List<Dish> dishes);

    }

    MasterListFragmentClicked mListFragmentClicked;
        public MasterListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListFragmentClicked = (MasterListFragmentClicked) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement MasterListFragmentClicked");
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        //If the device is landscape, take advantage of the space and display the recyclerView
        // in a GridLayout*/
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DishRecyclerViewAdapter recyclerViewAdapter = new DishRecyclerViewAdapter(
                    Utils.getDishes(getContext(), Utils.loadJSONFromAsset(getContext())), getContext(), this);
            RecyclerView recipeRecyclerView = rootView.findViewById(R.id.rv_recipes);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
            recipeRecyclerView.setLayoutManager(layoutManager);
            recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
            recipeRecyclerView.setAdapter(recyclerViewAdapter);
            return rootView;
        }

        //Normal recyclerView display
        DishRecyclerViewAdapter recyclerViewAdapter = new DishRecyclerViewAdapter(
                Utils.getDishes(getContext(), Utils.loadJSONFromAsset(getContext())), getContext(), this);
        RecyclerView recipeRecyclerView = rootView.findViewById(R.id.rv_recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRecyclerView.setAdapter(recyclerViewAdapter);
        return rootView;
    }


    //*Interface from DishRecyclerViewAdapter. It sends over the information of the item
    // that was clicked on. That information is then sent over to the host Activity (MainActivity)
    // to communicate with other fragments*/
    @Override
    public void onListItemClick(int clickedItemIndex, List<Dish> dishes) {
        mListFragmentClicked.onclickCallback(clickedItemIndex, dishes);
    }

    //Helper method for the GridLayout manager
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 800;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

}
