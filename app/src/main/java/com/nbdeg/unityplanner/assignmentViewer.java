package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignments;

import java.util.ArrayList;

public class assignmentViewer extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference assignmentDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments");
    String TAG = "Database";

    TextView assignmentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Finding views
        assignmentListView = (TextView) findViewById(R.id.assignment_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(assignmentViewer.this, addAssignment.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assignmentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    Log.i(TAG, "Assignment loaded: " + assignment.getAssignmentName());

                    if (assignment.getAssignmentPercent() != 100) {
                        assignmentListView.append(assignment.getAssignmentClassName() + "\n\n\n");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error getting assignments: " + databaseError.getMessage());
            }
        });
    }

}
