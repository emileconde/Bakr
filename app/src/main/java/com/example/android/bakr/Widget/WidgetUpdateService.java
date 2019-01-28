package com.example.android.bakr.Widget;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.android.bakr.model.Ingredient;
import com.example.android.bakr.ui.DishActivity;

/**
 * Created by conde on 6/5/2018.
 */

public class WidgetUpdateService extends IntentService {
    public static final String ACTION_UPDATE = "com.example.android.bakr.update_widget";
    private Ingredient[] mIngredients;
    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        if (intent != null && intent.getAction().equals(ACTION_UPDATE))
        {
            Bundle bundle = intent.getBundleExtra(DishActivity.INGREDIENT_BUNDLE);
            Parcelable []parcelables = bundle.getParcelableArray(DishActivity.INGREDIENT_INTENT_SERVICE_KEY);
            if (parcelables != null)
            {
                mIngredients = new Ingredient[parcelables.length];
                for (int i = 0; i < parcelables.length; i++)
                {
                    mIngredients[i] = (Ingredient) parcelables[i];
                }
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakrWidgetProvider.class));
            BakrWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds,mIngredients);
        }
    }

}
