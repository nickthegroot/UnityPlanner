package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.nbdeg.unityplanner.data.Classes;

public class classViewer extends AppCompatActivity {

    long classCount;
    private TextView classList;

    private final String TAG = "Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Finds views
        classList = (TextView) findViewById(R.id.class_list);

        // Gets Firebase Information
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("classes");

        classDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Classes mClass = userSnapshot.getValue(Classes.class);
                    Log.i(TAG, "Class loaded: " + mClass.getClassName());

                    classList.setText("");
                    classList.append(mClass.getClassName() + "\n\n\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading classes: " + databaseError.getMessage());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(classViewer.this, addClass.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
