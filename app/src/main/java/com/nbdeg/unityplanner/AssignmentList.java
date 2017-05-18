package com.nbdeg.unityplanner;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Utils.AssignmentHolder;
import com.nbdeg.unityplanner.Utils.Database;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentList extends Fragment {

    FirebaseRecyclerAdapter mAdapter;

    public AssignmentList() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_assignment);

        getActivity().setTitle("Assignments");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_assignment_list, container, false);
        RecyclerView assignmentView = (RecyclerView) mView.findViewById(R.id.assignment_list_view);
        LinearLayoutManager assignmentLayoutManager = new LinearLayoutManager(getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assignmentView.getContext(),
                assignmentLayoutManager.getOrientation());

        assignmentView.addItemDecoration(dividerItemDecoration);
        assignmentView.setLayoutManager(assignmentLayoutManager);


        Query ref = Database.dueAssignmentDb.orderByChild("dueDate");

        final Calendar dueCal = Calendar.getInstance();
        dueCal.set(Calendar.HOUR_OF_DAY, 0);
        dueCal.set(Calendar.MINUTE, 0);
        dueCal.set(Calendar.SECOND, -1);

        mAdapter = new FirebaseRecyclerAdapter<Assignment, AssignmentHolder>(Assignment.class, R.layout.database_assignment_view, AssignmentHolder.class, ref) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, Assignment assignment, int position) {
                viewHolder.setOnClick(assignment.getID());
                viewHolder.setName(assignment.getName());
                viewHolder.setDate(new Date(assignment.getDueDate()));
                viewHolder.setCourse(assignment.getDueCourse().getName());
                if (assignment.getDueDate() < dueCal.getTimeInMillis()) {
                    viewHolder.setLate(true);
                } else {
                    viewHolder.setLate(false);
                }
            }
        };

        assignmentView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
