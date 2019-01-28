package com.example.android.bakr.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Dish;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by conde on 5/10/2018.
 */

public class DishRecyclerViewAdapter extends RecyclerView.Adapter<DishRecyclerViewAdapter.RecipeViewHolder>{

    private List<Dish> mDishes;
    private Context mContext;
    private ListItemClickListener mOnclickListener;
    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, List<Dish> dishes);

    }

     DishRecyclerViewAdapter(List<Dish> dishes, Context context, ListItemClickListener listItemClickListener) {
        mDishes = dishes;
        mContext = context;
        mOnclickListener = listItemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.dish_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        final Dish dish = mDishes.get(position);
        Picasso.with(mContext).load(setImageBasedOnPosition(position))
                .placeholder(R.drawable.ic_image_black_32dp)
                .error(R.drawable.ic_broken_image_black_32dp)
                .into(holder.mRecipeImage);
        holder.mRecipeName.setText(dish.getName());
    }

    @Override
    public int getItemCount() {
        return mDishes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mRecipeImage;
        TextView mRecipeName;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            mRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnclickListener.onListItemClick(getAdapterPosition(), mDishes);
        }
    }

    //Chooses what to display in the recyclerView based on the position
    //in the ListView
    private int setImageBasedOnPosition(int position){
        switch(position){
            case 0:
                return R.drawable.nutellapie;

            case 1:
                return R.drawable.brownies;

            case 2:
                return R.drawable.yellowcake;

            case 3:
                return R.drawable.cheesecake;

        }
        return R.drawable.dummyfood;
    }




}
