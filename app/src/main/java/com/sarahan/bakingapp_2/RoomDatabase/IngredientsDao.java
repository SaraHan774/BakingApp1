package com.sarahan.bakingapp_2.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IngredientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientsEntity ingredientsEntity);

    @Query("SELECT * FROM myIngredientsTable")
    List<IngredientsEntity> getMyIngredients();

    @Query("DELETE FROM myIngredientsTable")  //delete every item in the database.
    void clearDatabase();

    @Query("SELECT count(*) FROM myIngredientsTable")
    int countDatabaseEntities();

}
