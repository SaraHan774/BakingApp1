package com.sarahan.bakingapp_2.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sarahan.bakingapp_2.POJOItems.IngredientsItem;

import java.util.ArrayList;

@Entity(tableName = "myIngredientsTable")
public class IngredientsEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "recipeName")
    private String recipeName;

    @TypeConverters(Converters.class)
    public final ArrayList<IngredientsItem> ingredientsItems;

    public IngredientsEntity(String recipeName, ArrayList<IngredientsItem> ingredientsItems){
        this.recipeName = recipeName;
        this.ingredientsItems = ingredientsItems;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public ArrayList<IngredientsItem> getIngredientsItems() {
        return ingredientsItems;
    }
}
