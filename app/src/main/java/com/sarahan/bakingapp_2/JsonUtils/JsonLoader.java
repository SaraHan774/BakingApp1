package com.sarahan.bakingapp_2.JsonUtils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class JsonLoader extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = JsonLoader.class.getSimpleName();
    private String mData;

    public JsonLoader(Context context){
        super(context);
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Log.d(LOG_TAG, "loading in background");
        String data = FetchJson.fetchJsonData();
        return data;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if(mData != null){
            Log.d(LOG_TAG, "using cached data");
            deliverResult(mData);
        }else{
            Log.d(LOG_TAG, "forceLoad()");
            forceLoad();
        }
    }


    @Override
    public void deliverResult(@Nullable String data) {
        super.deliverResult(data);

        Log.d(LOG_TAG, "deliver result");
        mData = data;
    }

}
