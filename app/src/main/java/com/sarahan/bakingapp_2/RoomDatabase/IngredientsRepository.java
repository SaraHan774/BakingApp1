package com.sarahan.bakingapp_2.RoomDatabase;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.sarahan.bakingapp_2.DetailActivity;
import com.sarahan.bakingapp_2.IngredientsWidget;

import java.util.List;

public class IngredientsRepository {
    private static final String LOG_TAG = IngredientsRepository.class.getSimpleName();

    private IngredientsDao ingredientsDao;
    private Context context;

    public IngredientsRepository(Context context){
        IngredientsDatabase ingredientsDatabase = IngredientsDatabase.getDatabase(context);
        ingredientsDao = ingredientsDatabase.ingredientsDao();
        this.context = context;
    }

    public List<IngredientsEntity> getIngredientsEntityList(){
        LoadAsync loadAsync = new LoadAsync(ingredientsDao);
        loadAsync.execute();
        Log.d(LOG_TAG, "getting items from database ... / ingredients : " + loadAsync.doInBackground());
        return loadAsync.doInBackground();
    }

    public int getCountEntities(){
        getCountAsync getCountAsync = new getCountAsync(ingredientsDao);
        getCountAsync.execute();
        Log.d(LOG_TAG, "counting how many entities are in the database : " + getCountAsync.doInBackground()); //???
        return getCountAsync.doInBackground();
    }


    public void insertIngredients(IngredientsEntity ingredientsEntity){
        Log.d(LOG_TAG, "inserting items ... ");
        new InsertAsync(ingredientsDao, context).execute(ingredientsEntity);
    }

    public void clearDatabase(){
        Log.d(LOG_TAG, "clearing items ... ");
        new ClearAsync(ingredientsDao).execute();
    }

    public static class InsertAsync extends AsyncTask<IngredientsEntity, Void, Void> {
        private IngredientsDao mAsyncTaskDao;
        private Context context;
        InsertAsync(IngredientsDao dao, Context context){
            mAsyncTaskDao = dao;
            this.context = context;
        }

        @Override
        protected Void doInBackground(IngredientsEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //sending broadcast to invoke onReceive() -> onUpdate() method in AppWidgetProvider class.
            Intent intent = new Intent(context, IngredientsWidget.class);
            intent.setAction(DetailActivity.ACTION_GET_RECIPE_INFO);
            context.sendBroadcast(intent);
        }
    }

    public static class ClearAsync extends AsyncTask<Void, Void, Void>{
        private IngredientsDao mAsyncTaskDao;

        public ClearAsync(IngredientsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.clearDatabase();
            return null;
        }

    }

    public static class LoadAsync extends AsyncTask<Void, Void, List<IngredientsEntity>>{
        private IngredientsDao mAsyncTaskDao;

        public LoadAsync(IngredientsDao dao){
            mAsyncTaskDao = dao;
        }


        @Override
        protected List<IngredientsEntity> doInBackground(Void... voids) {
            List<IngredientsEntity> ingredientsEntityList = mAsyncTaskDao.getMyIngredients();
            return ingredientsEntityList;
        }

    }

    public static class getCountAsync extends AsyncTask<Void, Void, Integer>{
        private IngredientsDao mAsyncTaskDao;
        private int count;
        public getCountAsync(IngredientsDao dao){mAsyncTaskDao = dao;}

        @Override
        protected Integer doInBackground(Void... voids) {
            count = mAsyncTaskDao.countDatabaseEntities();
            return count;
        }

    }



}