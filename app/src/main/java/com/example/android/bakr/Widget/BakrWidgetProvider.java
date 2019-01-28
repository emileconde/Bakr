package com.example.android.bakr.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakr.R;
import com.example.android.bakr.model.Ingredient;

/**
 * Implementation of App Widget functionality.
 */
public class BakrWidgetProvider extends AppWidgetProvider {

    public static Ingredient[] mIngredients;

    public BakrWidgetProvider() {
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], Ingredient[] ingredients) {

        mIngredients = ingredients;
        for(int appWidgetId : appWidgetIds)
        {
            Intent intent = new Intent(context, listViewService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakr_widget);
            views.setRemoteAdapter(R.id.widget_list_ingredients, intent);
            ComponentName component = new ComponentName(context, BakrWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_ingredients);
            appWidgetManager.updateAppWidget(component, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

