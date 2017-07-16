package com.nbdeg.unityplanner;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.nbdeg.unityplanner.utils.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class AssignmentWidget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent svcIntent = new Intent(context, WidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews widget = new RemoteViews(context.getPackageName(),
                R.layout.assignment_widget);
        widget.setRemoteAdapter(R.id.widget_list_view, svcIntent);

        Intent addAssignmentIntent = new Intent(context, CreateAssignment.class);
        PendingIntent addAssignmentPI = PendingIntent.getActivity(context, 0, addAssignmentIntent, 0);
        widget.setOnClickPendingIntent(R.id.widget_add_assignment, addAssignmentPI);

        Intent dashboardIntent = new Intent(context, Dashboard.class);
        PendingIntent dashboardPI = PendingIntent.getActivity(context, 0, dashboardIntent, 0);
        widget.setOnClickPendingIntent(R.id.widget_title, dashboardPI);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

