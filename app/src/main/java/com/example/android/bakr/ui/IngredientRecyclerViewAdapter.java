package com.example.android.bakr.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Ingredient;

import java.util.List;

/**
 * Created by conde on 5/10/2018.
 */

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredients;
    private Context mContext;

    IngredientRecyclerViewAdapter(){}

    public IngredientRecyclerViewAdapter(List<Ingredient> ingredients, Context context) {
        mIngredients = ingredients;
        mContext = context;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        final Ingredient ingredient = mIngredients.get(position);
        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientSizeAndMeasure.setText(ingredient.getQuantity() +" "+ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;
        TextView ingredientSizeAndMeasure;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.tv_ingredient_name);
            ingredientSizeAndMeasure = itemView.findViewById(R.id.tv_ingredient_serving_size_and_measure);
        }
    }

}
