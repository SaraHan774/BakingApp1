package com.sarahan.bakingapp_2.RoomDatabase;

import androidx.room.TypeConverter;

import com.sarahan.bakingapp_2.POJOItems.IngredientsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Converters {
//Gson gson = new Gson(); 을 꼭 할 필요가 있는지 모르겠음... 하려면 static 이어야 함...

    @TypeConverter
    public static ArrayList<IngredientsItem> fromIngredientsItems(String data){
        if(data == null){
            return null;
        }
        Type listType = new TypeToken<ArrayList<IngredientsItem>>(){}.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String toIngredientsItems(ArrayList<IngredientsItem> ingredientsItems){
        return new Gson().toJson(ingredientsItems);
    }
}
