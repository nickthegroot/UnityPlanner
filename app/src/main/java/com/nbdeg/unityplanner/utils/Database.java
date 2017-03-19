package com.nbdeg.unityplanner.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;

import java.util.ArrayList;

public class Database {

    private String TAG = "Database";
    private ArrayList<Assignments> assignmentList = new ArrayList<>();
    private ArrayList<Classes> classList = new ArrayList<>();

    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public DatabaseReference assignmentDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments");
    public DatabaseReference classDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("classes");

    // Gets all assignments
    public ArrayList<Assignments> getAssignments() {
        assignmentList.clear();
        assignmentDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    assignmentList.add(assignment);
                    Log.i(TAG, "Assignment loaded: " + assignment.getAssignmentName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading assignments: " + databaseError.getMessage());
            }
        });
        return assignmentList;
    }

    // Gets all classes
    public ArrayList<Classes> getClasses() {
        classList.clear();
        classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Classes mClass = userSnapshot.getValue(Classes.class);
                    classList.add(mClass);
                    Log.i(TAG, "Class loaded: " + mClass.getClassName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading classes: " + databaseError.getMessage());
            }
        });
        return classList;
    }

    public void addAssignment(Assignments assignment) {
        Log.i(TAG, "Creating assignment: " + assignment.getAssignmentName());
        String key = assignmentDb.push().getKey();
        assignmentDb.child(key).setValue(assignment);
    }

    public void addClass(Classes mClass) {
        Log.i(TAG, "Creating class: " + mClass.getClassName());
        classDb.push().setValue(mClass);
    }
}
