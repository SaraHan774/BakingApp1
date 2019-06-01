package com.sarahan.bakingapp_2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.sarahan.bakingapp_2.POJOItems.StepsItem;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity
    implements StepsDetailFragment.OnFragmentInteractionListener{
    private static final String LOG_TAG = StepsActivity.class.getSimpleName();

    private static final String ADAPTER_POSITION = "position";
    private static final String STEPS_ITEMS = "stepsItems";
    private static final String TWO_PANE = "isTwoPane";

    private ArrayList<StepsItem> stepsItems = new ArrayList<>();
    private static int position;
    private boolean isTwoPane;
    StepsDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_step_detail);

        Bundle data = getIntent().getExtras();
        Log.d(LOG_TAG, "Bundle data : " + data.toString());
        stepsItems = data.getParcelableArrayList(STEPS_ITEMS);
        position = data.getInt(ADAPTER_POSITION);
        isTwoPane = data.getBoolean(TWO_PANE);

        Toolbar toolbar =  findViewById(R.id.steps_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Step Description");


        if(savedInstanceState == null){
            fragment =
                    StepsDetailFragment.newInstance(position, stepsItems, isTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.steps_detail_container_single, fragment)
                    .commit();
        }
        else{
            fragment = (StepsDetailFragment) getSupportFragmentManager()
                            .getFragment(savedInstanceState, "fragment");
        }
        //스텝 누름 - 비디오 뜨는 경우 - 가로전환 - 비디오 화면 유지 - 세로전환 - 최초 클릭한 스텝으로 돌아감.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
        Log.d("onSaveInstanceState", "ON SAVE INSTANCE STATE");
    }


    @Override
    public void onFragmentInteraction(int position, ArrayList<StepsItem> stepsItems) {

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof StepsDetailFragment){
            StepsDetailFragment stepsDetailFragment = (StepsDetailFragment) fragment;
            stepsDetailFragment.setOnFragmentInteractionListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

}
