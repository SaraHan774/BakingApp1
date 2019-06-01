package com.sarahan.bakingapp_2;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.sarahan.bakingapp_2.POJOItems.RecipeInfoItem;
import com.sarahan.bakingapp_2.POJOItems.StepsItem;
import com.sarahan.bakingapp_2.RoomDatabase.IngredientsEntity;
import com.sarahan.bakingapp_2.RoomDatabase.IngredientsRepository;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity
    implements StepsDetailFragment.OnFragmentInteractionListener {

    private RecipeInfoItem recipeInfoItem;

    public static final String RECIPE_INFO = "RecipeInfo";
    public static final String ACTION_GET_RECIPE_INFO = "com.example.bakingapp_2.GET_RECIPE_INFO";

    private ImageView imageView;
    private Toolbar toolbar;
    private TextView tv_displayIngredientsOnWidget;

    private RecyclerView rv_ingredients;
    private RecyclerView rv_steps;
    private RecyclerView.Adapter adapter_ingredients;
    private RecyclerView.Adapter adapter_steps;
//default layout is one pane, not two.
    private boolean isTwoPane;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.img_detail_appbar);
        toolbar = findViewById(R.id.detail_toolbar);
        tv_displayIngredientsOnWidget = findViewById(R.id.tv_show_on_home_screen);

        if (savedInstanceState != null) {
            recipeInfoItem = savedInstanceState.getParcelable("recipeInfo");
        } else {
            Bundle data = getIntent().getExtras();
            if (data != null) {
                recipeInfoItem = data.getParcelable(RECIPE_INFO);
            }
        }


//when detail activity is created, check whether its two pane mode or not.
        if (findViewById(R.id.tablet_steps_detail_container) != null) {
            isTwoPane = true;
        }

        loadToolbar();
        setAdapters();

        tv_displayIngredientsOnWidget.setOnClickListener(v -> {

            IngredientsEntity ingredientsEntity = new IngredientsEntity(recipeInfoItem.getName(), recipeInfoItem.getIngredients());
            IngredientsRepository ingredientsRepository = new IngredientsRepository(getApplicationContext());

            //clear the database before inserting an item.
            ingredientsRepository.clearDatabase();
            ingredientsRepository.insertIngredients(ingredientsEntity);

        });


    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "on save instance state running");
        outState.putParcelable("recipeInfo", recipeInfoItem);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "on restore instance state running");
        recipeInfoItem = savedInstanceState.getParcelable("recipeInfo");
    }

    public void loadToolbar(){
        Log.d(LOG_TAG, "loading toolbars in detail activity");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(recipeInfoItem.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Picasso.get().load(recipeInfoItem.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit().centerInside().into(imageView);
        }
    }

    public void setAdapters(){
        Log.d(LOG_TAG, "set Adapters running");

        rv_ingredients = findViewById(R.id.rv_detail_ingredients);
        rv_ingredients.setHasFixedSize(true);
        rv_ingredients.setLayoutManager(new LinearLayoutManager(this));
        adapter_ingredients = new AdapterIngredients(this, recipeInfoItem.getIngredients());
        rv_ingredients.setAdapter(adapter_ingredients);

        rv_steps = findViewById(R.id.rv_detail_steps);
        rv_steps.setHasFixedSize(true);
        rv_steps.setLayoutManager(new LinearLayoutManager(this));
        adapter_steps = new AdapterSteps(this, recipeInfoItem.getSteps(), isTwoPane);
        rv_steps.setAdapter(adapter_steps);

    }

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    @Override
    public void onFragmentInteraction(int position, ArrayList<StepsItem> stepsItems) {
        Log.d(LOG_TAG, "onFragmentInteraction");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource(){
        return EspressoIdlingResource.getIdlingResource();
    }
}