package com.sarahan.bakingapp_2;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sarahan.bakingapp_2.POJOItems.IngredientsItem;
import com.sarahan.bakingapp_2.RoomDatabase.IngredientsEntity;
import com.sarahan.bakingapp_2.RoomDatabase.IngredientsRepository;


import java.util.ArrayList;
import java.util.List;


public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private static final String LOG_TAG = MyRemoteViewsFactory.class.getSimpleName();

    private ArrayList<IngredientsItem> ingredientsItems;
    private Context context;
    private List<String> collection = new ArrayList<>();
    private int appWidgetId;
    private RemoteViews remoteViews;
    private Intent intent;

    private IngredientsRepository ingredientsRepository;
    private List<IngredientsEntity> ingredientsEntityList;


    public MyRemoteViewsFactory(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        //why do I need to get this app widget id here?
        }


    @Override
    public void onCreate() {
    //called when the appwidget is created for the first time.

        Log.d(LOG_TAG, "remote views factory on create running ");
    }

    @Override
    public void onDataSetChanged() {
        //called whenever the appwidget is updated

        ingredientsRepository = new IngredientsRepository(context);
        if(ingredientsRepository.getCountEntities() != 0){
        Log.d(LOG_TAG, "size of database != 0");
            ingredientsEntityList = ingredientsRepository.getIngredientsEntityList();
            ingredientsItems = ingredientsEntityList.get(0).getIngredientsItems();
        }else {
            //a dummy item populates on the widget when there is nothing added in the database.
            collection.clear();
            for(int i = 1; i < 10 ; i++){
                collection.add("dummy item : " + i);
            }
        }
        Log.d(LOG_TAG, "remote views factory on dataset changed running , " + ingredientsItems);
        ingredientsRepository.clearDatabase();
    }



    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        //number of items that needs to be displayed in the app widget
        if(ingredientsItems != null) {
            return ingredientsItems.size();
        }else{
            return collection.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(ingredientsItems != null){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    android.R.layout.simple_list_item_1);
                                    //concatenating too many Strings ...?
            String widgetText = ingredientsItems.get(position).getIngredient()
                                + " "
                                + ingredientsItems.get(position).getQuantity()
                                + ingredientsItems.get(position).getMeasure();
            remoteViews.setTextViewText(android.R.id.text1, widgetText);

            Log.d(LOG_TAG, "getViewAt - reloading info...");
            return remoteViews;
        }else{
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    android.R.layout.simple_list_item_1);
            remoteViews.setTextViewText(android.R.id.text1, collection.get(position));
            return remoteViews;
        }
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    } //should be set to true or false ?




}
