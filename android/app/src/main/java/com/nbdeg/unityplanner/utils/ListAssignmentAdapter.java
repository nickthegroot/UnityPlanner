package com.nbdeg.unityplanner.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Assignment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListAssignmentAdapter extends RecyclerView.Adapter<AssignmentHolder> {

    private final ArrayList<Assignment> assignmentList;

    private final Calendar dueCal = Calendar.getInstance();


    public ListAssignmentAdapter(ArrayList<Assignment> assignmentList) {
        this.assignmentList = assignmentList;

        dueCal.set(Calendar.HOUR_OF_DAY, 0);
        dueCal.set(Calendar.MINUTE, 0);
        dueCal.set(Calendar.SECOND, -1);
    }

    @Override
    public AssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_assignment_view, parent, false);
        return new AssignmentHolder(view);
    }

    @Override
    public void onBindViewHolder(AssignmentHolder viewHolder, int position) {
        Assignment assignment = assignmentList.get(position);
        viewHolder.setOnClick(assignment.getID());
        viewHolder.setName(assignment.getName());
        viewHolder.setDate(new Date(assignment.getDueDate()));
        viewHolder.setCourse(assignment.getDueCourse().getName());
        viewHolder.setColor(assignment.getDueCourse().getColor());
        if (assignment.getDueDate() < dueCal.getTimeInMillis()) {
            viewHolder.setLate(true);
        } else {
            viewHolder.setLate(false);
        }
    }

    public Assignment getItem(int index) {
        return assignmentList.get(index);
    }

    public void removeAssignment(int index, Context context) {
        Assignment oldAssignment = getItem(index);
        Assignment newAssignment = new Assignment(oldAssignment);
        newAssignment.setPercentComplete(100);
        Database.editAssignment(newAssignment, oldAssignment, context);

        assignmentList.remove(index);
        notifyItemRemoved(index);
}

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }
}