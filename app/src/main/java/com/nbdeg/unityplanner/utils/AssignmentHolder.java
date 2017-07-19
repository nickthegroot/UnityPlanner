package com.nbdeg.unityplanner.utils;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nbdeg.unityplanner.AssignmentViewer;
import com.nbdeg.unityplanner.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssignmentHolder extends RecyclerView.ViewHolder {

    View assignment;
    TextView assignmentName;
    TextView assignmentDate;
    TextView assignmentCourse;
    View assignmentColor;
    View assignmentLate;

    SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());

    public AssignmentHolder(final View view) {
        super(view);
        assignment = view;
        assignmentColor = view.findViewById(R.id.assignment_color);
        assignmentName = (TextView) view.findViewById(R.id.assignment_name);
        assignmentDate = (TextView) view.findViewById(R.id.assignment_date);
        assignmentCourse = (TextView) view.findViewById(R.id.assignment_class);
        assignmentLate = view.findViewById(R.id.assignment_late);
    }

    public void setName(String name) {
        assignmentName.setText(name);
    }

    public void setDate(Date date) {
        assignmentDate.setText(formatter.format(date));
    }

    public void setCourse(String course) {
        assignmentCourse.setText(course);
    }

    public void setLate(boolean isLate) {
        if (isLate) {
            assignmentLate.setVisibility(View.VISIBLE);
        } else {
            assignmentLate.setVisibility(View.INVISIBLE);
        }
    }

    public void setOnClick(final String ID) {
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(assignment.getContext(), AssignmentViewer.class);
                intent.putExtra("ID", ID);
                assignment.getContext().startActivity(intent);
            }
        });
    }

    public void setColor(final int color) {
        assignmentColor.setBackgroundColor(color);
    }
}
