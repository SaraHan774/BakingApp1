package com.sarahan.bakingapp_2;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.sarahan.bakingapp_2.RoomDatabase.IngredientsRepository;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
    private static final String LOG_TAG = IngredientsWidget.class.getSimpleName();


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.ingredients_widget);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            setRemoteAdapter(context.getApplicationContext(), views, appWidgetId);
        }else{
            setRemoteAdapterV11(context.getApplicationContext(), views, appWidgetId);
        }

        IngredientsRepository ingredientsRepository = new IngredientsRepository(context);
        if(ingredientsRepository.getCountEntities() != 0){
            views.setTextViewText(R.id.widget_header, ingredientsRepository.getIngredientsEntityList().get(0).getRecipeName());
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
        //invoking onDataSetChanged in RemoteViewsFactory class
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d("widget", "AppWidgetProvider - onUpdate - invoking updateAppWidget method");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Toast.makeText(context, "Widget has been updated! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views, int appWidgetId){
        Log.d(LOG_TAG, "setRemoteAdapter()");
        Intent serviceIntent = new Intent(context, MyRemoteViewsService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        context.startService(serviceIntent);
        views.setRemoteAdapter(R.id.widget_list, serviceIntent);
    }

    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views, int appWidgetId){
        Intent serviceIntent = new Intent(context, MyRemoteViewsService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(0, R.id.widget_list, serviceIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context.getApplicationContext(), IngredientsWidget.class);
        int [] appWidgetIds  = appWidgetManager.getAppWidgetIds(componentName);

        if(intent.getAction().equals(DetailActivity.ACTION_GET_RECIPE_INFO)){

            //invoke onUpdate() method upon receiving a broadcast from Detail Activity.
           onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

