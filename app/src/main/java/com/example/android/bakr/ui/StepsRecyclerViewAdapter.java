package com.example.android.bakr.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Step;

import java.util.List;

/**
 * Created by conde on 5/10/2018.
 */

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.StepsViewHolder> {

    private List<Step> mSteps;
    Context mContext;
    private StepsOnclickCallBack mOnclickCallBack;

    StepsRecyclerViewAdapter(){}

    public interface StepsOnclickCallBack{
        void onClickCallback(int position, List<Step> steps);
    }


    public StepsRecyclerViewAdapter(List<Step> steps, Context context, StepsOnclickCallBack callBack ) {
        mSteps = steps;
        mContext = context;
        mOnclickCallBack = callBack;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        final Step step = mSteps.get(position);
        holder.shortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView shortDescription;
        StepsViewHolder(View itemView) {
            super(itemView);
            shortDescription = itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mOnclickCallBack.onClickCallback(getAdapterPosition(), mSteps);

        }
    }
}
