package com.nbdeg.unityplanner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;

public class assignmentFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public assignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        // Gets Firebase Information
        database db = new database();
        DatabaseReference assignmentDb = db.assignmentDb;
        ListView assignmentView = (ListView) view.findViewById(R.id.assignment_list);

        FirebaseListAdapter mAdapter = new FirebaseListAdapter<Assignments>(getActivity(), Assignments.class, android.R.layout.two_line_list_item, assignmentDb) {
            @Override
            protected void populateView(View view, Assignments assignment, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(assignment.getAssignmentName());
                ((TextView)view.findViewById(android.R.id.text2)).setText(assignment.getAssignmentClassName());

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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
