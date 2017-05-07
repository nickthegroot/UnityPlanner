package com.nbdeg.unityplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Utils.Database;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentList extends Fragment {

    FirebaseListAdapter mAdapter;

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
        ListView assignmentView = (ListView) mView.findViewById(R.id.assignment_list_view);
        DatabaseReference ref = Database.dueAssignmentDb;

        mAdapter = new FirebaseListAdapter<Assignment>(getActivity(), Assignment.class, R.layout.database_assignment_view, ref) {
            @Override
            protected void populateView(View view, final Assignment assignment, int position) {
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
                ((TextView)view.findViewById(R.id.assignment_name)).setText(assignment.getName());
                ((TextView)view.findViewById(R.id.assignment_date)).setText(formatter.format(new Date(assignment.getDueDate())));
                if (assignment.getDueCourse() != null) {
                    ((TextView) view.findViewById(R.id.assignment_class)).setText(assignment.getDueCourse().getName());
                }
                if (assignment.getDueDate() < System.currentTimeMillis()) {
                    view.findViewById(R.id.assignment_late).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.assignment_late).setVisibility(View.INVISIBLE);
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AssignmentViewer.class);
                        intent.putExtra("ID", assignment.getID());
                        startActivity(intent);
                    }
                });
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
