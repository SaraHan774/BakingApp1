package com.sarahan.bakingapp_2;

import android.content.Intent;
import android.widget.RemoteViewsService;



public class MyRemoteViewsService extends RemoteViewsService {
//just consider this class as the class which tells the ListView of
//app widget to take what type of data.
//by data meaning what RemoteViewsFactory
//adapter for the list view.

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
