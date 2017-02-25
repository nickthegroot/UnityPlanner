package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Classes;

public class classViewer extends AppCompatActivity {

    private TextView classList;

    private final String TAG = "Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Gets Firebase Information
        database db = new database();
        DatabaseReference classDb = db.classDb;
        ListView classesView = (ListView) findViewById(R.id.classes_view);

        FirebaseListAdapter mAdapter = new FirebaseListAdapter<Classes>(this, Classes.class, android.R.layout.two_line_list_item, classDb) {
            @Override
            protected void populateView(View view, Classes mClass, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(mClass.getClassName());
                ((TextView)view.findViewById(android.R.id.text2)).setText(mClass.getClassTeacher());

            }
        };

        classesView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(classViewer.this, addClass.class));
            }
        });
    }
}
