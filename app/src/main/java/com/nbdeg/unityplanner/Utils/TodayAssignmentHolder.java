package com.nbdeg.unityplanner.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nbdeg.unityplanner.AssignmentViewer;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TodayAssignmentHolder extends ArrayAdapter<Assignment> {

    SimpleDateFormat assignmentFormatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());

    public TodayAssignmentHolder(Context context, ArrayList<Assignment> assignments) {
        super(context, 0, assignments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final Assignment assignment = getItem(position);
        final Calendar dueCal = Calendar.getInstance();
        dueCal.set(Calendar.HOUR_OF_DAY, 0);
        dueCal.set(Calendar.MINUTE, 0);
        dueCal.set(Calendar.SECOND, -1);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.database_assignment_view, parent, false);
        }

        ((TextView)view.findViewById(R.id.assignment_name)).setText(assignment.getName());
        ((TextView)view.findViewById(R.id.assignment_class)).setText(assignment.getDueCourse().getName());
        ((TextView)view.findViewById(R.id.assignment_date)).setText(assignmentFormatter.format(assignment.getDueDate()));
        if (assignment.getDueDate() < dueCal.getTimeInMillis()) {
            view.findViewById(R.id.assignment_late).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.assignment_late).setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AssignmentViewer.class);
                intent.putExtra("ID", assignment.getID());
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}
