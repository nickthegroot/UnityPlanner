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

public class TomorrowAssignmentHolder extends ArrayAdapter<Assignment> {

    SimpleDateFormat assignmentFormatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());

    public TomorrowAssignmentHolder(Context context, ArrayList<Assignment> assignments) {
        super(context, 0, assignments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        final Assignment assignment = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.database_assignment_view, parent, false);
        }

        ((TextView)view.findViewById(R.id.assignment_name)).setText(assignment.getName());
        ((TextView)view.findViewById(R.id.assignment_class)).setText(assignment.getDueCourse().getName());
        ((TextView)view.findViewById(R.id.assignment_date)).setText(assignmentFormatter.format(assignment.getDueDate()));
        (view.findViewById(R.id.assignment_color)).setBackgroundColor(assignment.getDueCourse().getColor());

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
