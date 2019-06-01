package com.sarahan.bakingapp_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sarahan.bakingapp_2.POJOItems.StepsItem;

import java.util.ArrayList;

public class AdapterSteps extends RecyclerView.Adapter<AdapterSteps.StepsViewHolder> {
    private static final String LOG_TAG = AdapterSteps.class.getSimpleName();

    private static final String ADAPTER_POSITION = "position";
    private static final String STEPS_ITEMS = "stepsItems";
    private static final String TWO_PANE = "isTwoPane";
    private Context context;
    private ArrayList<StepsItem> stepsItems;
    private boolean isTwoPane;


    public AdapterSteps(Context context, ArrayList<StepsItem> stepsItems, boolean isTwoPane){
        this.context = context;
        this.stepsItems = stepsItems;
        this.isTwoPane = isTwoPane;
    }

    @NonNull
    @Override
    public AdapterSteps.StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_steps, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSteps.StepsViewHolder holder, int position) {
        StepsItem stepsItem = stepsItems.get(position);

        if(stepsItem.getId() != 0)
            holder.tv_step_number.setText(String.valueOf(stepsItem.getId()));

        if(stepsItem.getShortDes() != null){
        holder.tv_step_description.setText(stepsItem.getShortDes());
        }

        holder.view.setOnClickListener(v -> {
            Log.d(LOG_TAG, "Boolean is two pane: " + isTwoPane);
            if(isTwoPane){
                int position1 = holder.getAdapterPosition();
                StepsDetailFragment fragment = StepsDetailFragment.newInstance(position1, stepsItems, isTwoPane) ;
                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.tablet_steps_detail_container, fragment)
                        .commit();
            }else{
                Intent intent = new Intent(context, StepsActivity.class);
                intent.putExtra(STEPS_ITEMS, stepsItems)
                        .putExtra(ADAPTER_POSITION, stepsItem.getId())
                        .putExtra(TWO_PANE, isTwoPane);
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return stepsItems.size();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView tv_step_number;
        TextView tv_step_description;

        private StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_step_number = itemView.findViewById(R.id.tv_detail_step_number);
            tv_step_description = itemView.findViewById(R.id.tv_detail_step_description);
        }
    }
}
