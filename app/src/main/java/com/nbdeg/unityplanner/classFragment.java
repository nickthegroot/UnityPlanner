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
import com.nbdeg.unityplanner.data.Classes;
import com.nbdeg.unityplanner.utils.ClassesHolder;
import com.nbdeg.unityplanner.utils.Database;

public class classFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter mAdapter;

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
        Database db = new Database();
        DatabaseReference classDb = db.classDb;

        // Displaying Data
        RecyclerView classesView = (RecyclerView) view.findViewById(R.id.class_list);
        classesView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FirebaseRecyclerAdapter<Classes, ClassesHolder>(Classes.class, R.layout.classes_layout, ClassesHolder.class, classDb) {
            @Override
            protected void populateViewHolder(ClassesHolder viewHolder, final Classes mClass, final int position) {
                viewHolder.setEverything(mClass);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), editClass.class);
                        intent.putExtra("ID", mAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
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
        mAdapter.cleanup();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
