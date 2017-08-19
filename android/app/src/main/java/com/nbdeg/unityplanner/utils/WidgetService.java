package com.nbdeg.unityplanner.utils;

/**
 * Created by nbdeg on 6/4/2017.
 */

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewFactory(this.getApplicationContext(), intent);
    }
}