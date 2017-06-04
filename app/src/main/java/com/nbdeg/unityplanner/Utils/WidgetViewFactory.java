package com.nbdeg.unityplanner.Utils;

/**
 * Created by nbdeg on 6/4/2017.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
    SimpleDateFormat assignmentFormatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
    private Context ctxt=null;
    private ArrayList<Assignment> assignments = new ArrayList<>();

    public WidgetViewFactory(Context ctxt) {
        this.ctxt=ctxt;
    }

    @Override
    public void onCreate() {
        Database.dueAssignmentDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Assignment assignment = userSnapshot.getValue(Assignment.class);
                    assignments.add(assignment);
                }
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

        card.setTextViewText(R.id.assignment_name ,assignment.getName());
        card.setTextViewText(R.id.assignment_class, assignment.getDueCourse().getName());
        card.setTextViewText(R.id.assignment_date, assignmentFormatter.format(assignment.getDueDate()));
        card.setInt(R.id.assignment_color, "setBackgroundColor", assignment.getDueCourse().getColor());
        if (assignment.getDueDate() < dueCal.getTimeInMillis()) {
            card.setInt(R.id.assignment_late, "setVisibility", View.VISIBLE);
        } else {
            card.setInt(R.id.assignment_late, "setVisibility", View.INVISIBLE);
        }

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