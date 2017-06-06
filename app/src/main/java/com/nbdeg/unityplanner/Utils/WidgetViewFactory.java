package com.nbdeg.unityplanner.Utils;

/**
 * Created by nbdeg on 6/4/2017.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.AssignmentWidget;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
    SimpleDateFormat assignmentFormatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
    private Context ctxt=null;
    private int appWidgetID;
    private ArrayList<Assignment> assignments = new ArrayList<>();

    public WidgetViewFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Database.dueAssignmentDb.orderByChild("dueDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Assignment assignment = userSnapshot.getValue(Assignment.class);
                    assignments.add(assignment);
                }
                AppWidgetManager.getInstance(ctxt).notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_list_view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        Log.d("Widget", "getCount: " + assignments.size());
        return assignments.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews card=new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_layout);

        Assignment assignment = assignments.get(position);

        final Calendar dueCal = Calendar.getInstance();
        dueCal.set(Calendar.HOUR_OF_DAY, 0);
        dueCal.set(Calendar.MINUTE, 0);
        dueCal.set(Calendar.SECOND, -1);

        card.setTextViewText(R.id.widget_assignment_name ,assignment.getName());
        card.setTextViewText(R.id.widget_assignment_class, assignment.getDueCourse().getName());
        card.setTextViewText(R.id.widget_assignment_date, assignmentFormatter.format(assignment.getDueDate()));
        if (assignment.getDueDate() < dueCal.getTimeInMillis()) {
            card.setInt(R.id.widget_assignment_late, "setVisibility", View.VISIBLE);
        } else {
            card.setInt(R.id.widget_assignment_late, "setVisibility", View.INVISIBLE);
        }

        Intent clickIntent = new Intent(ctxt, AssignmentWidget.class);
        clickIntent.putExtra("ID", assignment.getID());
        PendingIntent clickPI = PendingIntent
                .getActivity(ctxt,
                        position,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


        card.setOnClickPendingIntent(appWidgetID, clickPI);

        return card;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}