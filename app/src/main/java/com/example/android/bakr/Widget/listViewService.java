package com.example.android.bakr.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Ingredient;

/**
 * Created by conde on 6/2/2018.
 */

public class listViewService extends RemoteViewsService{

    @Override
    public ListRemoteViewFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }
}


 class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Ingredient[] mIngredient;

    ListRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngredient = BakrWidgetProvider.mIngredients;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredient == null)
            return 0;
        return mIngredient.length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_text_layout);
        views.setTextViewText(R.id.tv_ingredient_widget, mIngredient[i].getName());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
