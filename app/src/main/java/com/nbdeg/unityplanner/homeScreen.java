package com.nbdeg.unityplanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.utils.AssignmentHolder;
import com.nbdeg.unityplanner.utils.Database;

public class homeScreen extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter mAdapter;

    private boolean haveAssignmentsDue = false;
    private Database db = new Database();

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

        final TextView haveAssignmentsDueView = (TextView) view.findViewById(R.id.assignments_due);
        final RecyclerView assignmentsDue = (RecyclerView) view.findViewById(R.id.home_assignment_list);

        DatabaseReference assignmentDb = db.assignmentDb;
        assignmentsDue.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FirebaseRecyclerAdapter<Assignments, AssignmentHolder>(Assignments.class, R.layout.assignment_layout, AssignmentHolder.class, assignmentDb) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, final Assignments assignment, final int position) {
                if (assignment.getPercent() != 100) {
                    // Have assignments due - set haveAssignmentsDue to true
                    if (!haveAssignmentsDue) {
                        haveAssignmentsDue = true;
                        haveAssignmentsDueView.setVisibility(View.INVISIBLE);
                        assignmentsDue.setVisibility(View.VISIBLE);
                    }
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter.cleanup();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
