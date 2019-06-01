package com.sarahan.bakingapp_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.sarahan.bakingapp_2.POJOItems.IngredientsItem;
import com.sarahan.bakingapp_2.POJOItems.RecipeInfoItem;
import com.sarahan.bakingapp_2.POJOItems.StepsItem;
import com.sarahan.bakingapp_2.JsonUtils.JsonLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.sarahan.bakingapp_2.JsonUtils.JsonKeys.*;

public class MainActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<String> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterMain;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.rv_main);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        getSupportLoaderManager().initLoader(0, null, MainActivity.this);

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new JsonLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        //for UI testing

        parseRecipeInfoJsonString(data);
        adapterMain = new AdapterMain(MainActivity.this, recipeInfoItemArrayList);
        recyclerView.setAdapter(adapterMain);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    //parse JSON data
    private ArrayList<StepsItem> stepsItemArrayList;
    private ArrayList<IngredientsItem> ingredientsItemArrayList;
    private ArrayList<RecipeInfoItem> recipeInfoItemArrayList = new ArrayList<>();
    private JSONArray ingredientsJsonArr;
    private JSONArray stepsJsonArr;

    public void parseRecipeInfoJsonString(String data){

        try{
            if(recipeInfoItemArrayList != null){
                recipeInfoItemArrayList.clear();
            }
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt(ELEMENT_ID);
                String name = object.getString(ITEM_NAME);
                ingredientsJsonArr = object.getJSONArray(INGREDIENTS);
                stepsJsonArr = object.getJSONArray(STEPS);

                parseIngredientsJsonArr(ingredientsJsonArr);
                parseStepsJsonArr(stepsJsonArr);

                int servings = object.getInt(SERVINGS);
                String image = "";
                    switch (i){
                        case 0:
                            image = NUTELLA_PIE;
                            break;
                        case 1:
                            image = BROWNIES;
                            break;
                        case 2:
                            image = YELLOW_CAKE;
                            break;
                        case 3:
                            image = CHEESE_CAKE;
                            break;
                    }

                    Log.d(LOG_TAG, "JSON items : " + id +", "+ name+", " + servings+", " + image
                + "," + ingredientsJsonArr.toString() + " , "+ stepsJsonArr.toString()) ;

                recipeInfoItemArrayList.add(new RecipeInfoItem(id, name, arrayLists_ingredients.get(i),
                        arrayLists_steps.get(i), servings, image));
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    ArrayList<ArrayList<StepsItem>> arrayLists_steps = new ArrayList<>();
    public void parseStepsJsonArr(JSONArray stepsJsonArr){
        stepsItemArrayList = new ArrayList<>();
        if(stepsItemArrayList != null){
            stepsItemArrayList.clear();
        }
        for(int i = 0; i < stepsJsonArr.length(); i++){
            try{
                JSONObject object = stepsJsonArr.getJSONObject(i);
                int id = object.getInt(STEP_ID);
                String shortDes = object.getString(SHORT_DES);
                String longDes = object.getString(LONG_DES);
                String videoURL = object.getString(VIDEO_URL);
                String thumbnailURL = object.getString(THUMBNAIL_URL);

                stepsItemArrayList.add(new StepsItem(id, shortDes, longDes, videoURL, thumbnailURL));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        arrayLists_steps.add(stepsItemArrayList);
    }


    ArrayList<ArrayList<IngredientsItem>> arrayLists_ingredients = new ArrayList<>();
    public void parseIngredientsJsonArr(JSONArray ingredientsJsonArr){
        ingredientsItemArrayList = new ArrayList<>();
        if(ingredientsItemArrayList != null){
            ingredientsItemArrayList.clear();
        }
         for(int i = 0; i < ingredientsJsonArr.length(); i++){
            try {
                JSONObject object = ingredientsJsonArr.getJSONObject(i);
                int quantity = object.getInt(QUANTITY);
                String measure = object.getString(MEASURE);
                String ingredient = object.getString(INGREDIENT);
                ingredientsItemArrayList.add(new IngredientsItem(quantity, measure, ingredient));
            }catch (JSONException e){
                e.printStackTrace();
            }
         }
        arrayLists_ingredients.add(ingredientsItemArrayList);
        Log.d(LOG_TAG, "json arr len: " + ingredientsJsonArr.length() + "arr list len : " + ingredientsItemArrayList.size());
    }



}
