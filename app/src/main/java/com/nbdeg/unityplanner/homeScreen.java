package com.nbdeg.unityplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.utils.AssignmentHolder;
import com.nbdeg.unityplanner.utils.Database;

public class homeScreen extends Fragment {
    private FirebaseRecyclerAdapter mAdapter;

    TextView haveAssignmentsDueView;
    RecyclerView assignmentsDue;

    boolean haveAssignmentsDue = false;

    public homeScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        haveAssignmentsDueView = (TextView) view.findViewById(R.id.assignments_due);
        assignmentsDue = (RecyclerView) view.findViewById(R.id.home_assignment_list);
        assignmentsDue.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FirebaseRecyclerAdapter<Assignments, AssignmentHolder>(Assignments.class, R.layout.assignment_layout, AssignmentHolder.class, Database.dueAssignmentsDb.orderByChild("dueDate").limitToFirst(5)) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, final Assignments assignment, final int position) {
                // Have assignments due - set haveAssignmentsDue to true
                if (!haveAssignmentsDue && assignment.getPercent() != 100) {
                    haveAssignmentsDue = true;
                    haveAssignmentsDueView.setVisibility(View.INVISIBLE);
                    assignmentsDue.setVisibility(View.VISIBLE);
                }
                if (assignment.getPercent() != 100) {
                    viewHolder.setEverything(assignment);
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), editAssignment.class);
                            intent.putExtra("ID", mAdapter.getRef(position).getKey());
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        assignmentsDue.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter.cleanup();
    }
}
