package com.nbdeg.unityplanner.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Assignments;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssignmentHolder extends RecyclerView.ViewHolder {
    private final TextView mNameField;
    private final TextView mDueField;
    private final TextView mClassField;
    private final View view;

    public AssignmentHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.view_assignment_name);
        mDueField = (TextView) itemView.findViewById(R.id.view_assignment_due);
        mClassField = (TextView) itemView.findViewById(R.id.view_assignment_class);
        view = this.itemView;
    }

    public void setEverything(Assignments assignment) {
        mNameField.setText(assignment.getAssignmentName());
        if (assignment.getAssignmentDueDate() != null) {
            Date dueDate = new Date(assignment.getAssignmentDueDate());
            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, yyyy");
            mDueField.setText(formatter.format(dueDate));
        }
        mClassField.setText(assignment.getAssignmentClassName());
    }

    public void setName(String name) {
        mNameField.setText(name);
    }

    public void setDueDate(String date) {
        mDueField.setText(date);
    }

    public void setClassField(String mClass) {
        mClassField.setText(mClass);
    }
}