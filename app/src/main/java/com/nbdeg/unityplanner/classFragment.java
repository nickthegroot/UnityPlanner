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
import com.nbdeg.unityplanner.data.Classes;

public class classFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public classFragment() {
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
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        // Gets Firebase Information
        database db = new database();
        DatabaseReference classDb = db.classDb;
        ListView classesView = (ListView) view.findViewById(R.id.classes_view);

        FirebaseListAdapter mAdapter = new FirebaseListAdapter<Classes>(getActivity(), Classes.class, android.R.layout.two_line_list_item, classDb) {
            @Override
            protected void populateView(View view, Classes mClass, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(mClass.getClassName());
                ((TextView)view.findViewById(android.R.id.text2)).setText(mClass.getClassTeacher());
            }
        };

        classesView.setAdapter(mAdapter);
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
