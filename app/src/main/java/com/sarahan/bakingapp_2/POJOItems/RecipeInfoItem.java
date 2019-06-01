package com.sarahan.bakingapp_2.POJOItems;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RecipeInfoItem implements Parcelable {

    private int id;
    private String name;
    private ArrayList<IngredientsItem> ingredients;
    private ArrayList<StepsItem> steps;
    private int servings;
    private String image;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<IngredientsItem> getIngredients() {
        return ingredients;
    }

    public ArrayList<StepsItem> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public RecipeInfoItem(int id, String name, ArrayList<IngredientsItem> ingredients,
                          ArrayList<StepsItem> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    protected RecipeInfoItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(IngredientsItem.CREATOR);
        this.steps = in.createTypedArrayList(StepsItem.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Creator<RecipeInfoItem> CREATOR = new Creator<RecipeInfoItem>() {
        @Override
        public RecipeInfoItem createFromParcel(Parcel source) {
            return new RecipeInfoItem(source);
        }

        @Override
        public RecipeInfoItem[] newArray(int size) {
            return new RecipeInfoItem[size];
        }
    };
}