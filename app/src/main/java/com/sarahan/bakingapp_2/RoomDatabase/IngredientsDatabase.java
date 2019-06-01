package com.sarahan.bakingapp_2.RoomDatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {IngredientsEntity.class}, version = 1, exportSchema = false)
public abstract class IngredientsDatabase extends RoomDatabase {

    public abstract IngredientsDao ingredientsDao();
    private static final String DATABASE_NAME = "ingredients_database";
    private static volatile IngredientsDatabase INSTANCE;

    public static IngredientsDatabase getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (IngredientsDatabase.class){
                if(INSTANCE == null){
                    Log.d("DATABASE", "getDatabase ... INSTANCE == null, building database.");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            IngredientsDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() //should not allow main thread query, but didn't know a better way to do this.
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{
        private final IngredientsDao ingredientsDao;

        PopulateDbAsync(IngredientsDatabase database){
            ingredientsDao = database.ingredientsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("DATABASE", "PopulateDbAsync - doInBackground");
            ingredientsDao.clearDatabase();
            return null;
        }
    }
}
