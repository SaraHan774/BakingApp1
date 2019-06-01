package com.sarahan.bakingapp_2;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sarahan.bakingapp_2.POJOItems.IngredientsItem;

import java.util.ArrayList;

public class AdapterIngredients extends RecyclerView.Adapter<AdapterIngredients.IngredientsViewHolder>{
    private static final String LOG_TAG = AdapterIngredients.class.getSimpleName();

    private Context context;
    private ArrayList<IngredientsItem> ingredientsItems;

    public AdapterIngredients(Context context, ArrayList<IngredientsItem> ingredientsItems){
        this.context = context;
        this.ingredientsItems = ingredientsItems;
    }

    @NonNull
    @Override
    public AdapterIngredients.IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngredients.IngredientsViewHolder holder, int position) {
        IngredientsItem ingredientsItem = ingredientsItems.get(position);
        int quantity = ingredientsItem.getQuantity();
        String measure = ingredientsItem.getMeasure();
        String ingredient = ingredientsItem.getIngredient();

        holder.tv_Ingredient.setText(ingredient + " " + quantity + measure);
    }

    @Override
    public int getItemCount() {
        return ingredientsItems.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_Ingredient;
        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Ingredient = itemView.findViewById(R.id.tv_detail_ingredient);
        }
    }
}
