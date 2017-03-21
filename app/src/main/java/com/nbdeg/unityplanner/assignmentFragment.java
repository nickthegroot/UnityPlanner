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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.utils.AssignmentHolder;
import com.nbdeg.unityplanner.utils.Database;

public class assignmentFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter mAdapter;

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
        Database db = new Database();
        final DatabaseReference assignmentDb = db.assignmentDb;

        // Displaying Data
        final RecyclerView assignmentView = (RecyclerView) view.findViewById(R.id.assignment_list);
        assignmentView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FirebaseRecyclerAdapter<Assignments, AssignmentHolder>(Assignments.class, R.layout.assignment_layout, AssignmentHolder.class, assignmentDb) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, final Assignments assignment, final int position) {
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
        };
        assignmentView.setAdapter(mAdapter);
        return view;
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
        void onFragmentInteraction(Uri uri);
    }
}
