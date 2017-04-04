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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.utils.AssignmentHolder;
import com.nbdeg.unityplanner.utils.Database;

public class assignmentFragment extends Fragment {
    private FirebaseRecyclerAdapter mDueAdapter;

    public assignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);

        // Getting Data
        Query query = Database.allAssignmentsDb.orderByChild("dueDate");

        // Displaying Data
        final RecyclerView assignmentView = (RecyclerView) view.findViewById(R.id.assignment_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        assignmentView.setLayoutManager(layoutManager);

        mDueAdapter = new FirebaseRecyclerAdapter<Assignments, AssignmentHolder>(Assignments.class, R.layout.assignment_layout, AssignmentHolder.class, query) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, final Assignments assignment, final int position) {
                viewHolder.setEverything(assignment);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), editAssignment.class);
                        intent.putExtra("ID", mDueAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };

        assignmentView.setAdapter(mDueAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDueAdapter.cleanup();
    }
}
