package com.sarahan.bakingapp_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sarahan.bakingapp_2.POJOItems.RecipeInfoItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.RecipeInfoViewHolder>{
        private static final String LOG_TAG = AdapterMain.class.getSimpleName();
        public static final String RECIPE_INFO = "RecipeInfo";



    private Context context;
    private ArrayList<RecipeInfoItem> recipeInfoItems;
        //    private final boolean mTwoPane;

        public AdapterMain(Context context, ArrayList<RecipeInfoItem> recipeInfoItems){
            super();
            this.context = context;
            this.recipeInfoItems = recipeInfoItems;
        }

        @NonNull
        @Override
        public AdapterMain.RecipeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_holder_main, parent, false);
            return new RecipeInfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeInfoViewHolder holder, int position) {
            Log.d(LOG_TAG, "on bind view holder");
            final RecipeInfoItem recipeInfoItem = recipeInfoItems.get(position);

            //displayed in the Main Activity
            holder.recipeName.setText(recipeInfoItem.getName());
            holder.servings.setText("" + recipeInfoItem.getServings()); //setText 안에는 반드시 String type이 들어가야 한다.
            Picasso.get().load(recipeInfoItem.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit().centerInside().into(holder.recipeImage);

            //Data sent over to the Detail Activity
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(RECIPE_INFO, recipeInfoItem)
                    .putExtra("ADAPTER", holder.getAdapterPosition());

                    Log.d(LOG_TAG, "recipeInfoItem passed to detail : " + recipeInfoItem.getIngredients().size());
                    Log.d(LOG_TAG, "adapter position : " + holder.getAdapterPosition());

                    context.startActivity(intent);
                    //detail list 에 대한 정보를 Detail activity 로 넘겨준다.
            }
        });
        }

        @Override
        public int getItemCount() {
            Log.d(LOG_TAG, "get item count: " + recipeInfoItems.size());
            return recipeInfoItems.size();
        }




    class RecipeInfoViewHolder extends RecyclerView.ViewHolder{
            TextView recipeName;
            TextView servings;
            ImageView recipeImage;

            public RecipeInfoViewHolder(View itemView){
                super(itemView);
                recipeName = itemView.findViewById(R.id.tv_vh_main_name);
                servings = itemView.findViewById(R.id.tv_vh_main_servings);
                recipeImage = itemView.findViewById(R.id.img_vh_main);
            }
        }

}

