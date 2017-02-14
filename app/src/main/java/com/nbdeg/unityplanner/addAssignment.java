package com.nbdeg.unityplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbdeg.unityplanner.data.Assignments;

import java.util.ArrayList;

public class addAssignment extends AppCompatActivity  {

    private EditText mAssignmentName;
    private EditText mDueDate;
    private EditText mExtraInfo;
    private Spinner mDueClass;
    private int percentComplete = 0;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference assignmentDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        // Gets class list
        Bundle extras = getIntent().getExtras();
        ArrayList<String> classListNames = extras.getStringArrayList("classListNames");


        // Gets firebase database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        assignmentDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments");

        // Find view by ID calls
        mAssignmentName = (EditText) findViewById(R.id.assignment_name);
        mDueDate = (EditText) findViewById(R.id.due_date_edittext);
        mExtraInfo = (EditText) findViewById(R.id.extra_homework_info);
        mDueClass = (Spinner) findViewById(R.id.class_spinner);
        SeekBar mPercentComplete = (SeekBar) findViewById(R.id.percentComplete);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, classListNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDueClass.setAdapter(adapter);

        // Sets DueDate EditText to open a datepicker when clicked
        new EditTextDatePicker(this, R.id.due_date_edittext);

        mPercentComplete.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                percentComplete = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // Adds a SAVE button to the Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    // Gets and saves information when SAVE is clicked

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Getting information from views
        String dueDate = mDueDate.getText().toString();
        String assignmentName = mAssignmentName.getText().toString();
        String extraInfo = mExtraInfo.getText().toString();
        String dueClass = mDueClass.getItemAtPosition(mDueClass.getSelectedItemPosition()).toString();

        mFirebaseAnalytics.logEvent("Assignment Created", null);
        Log.i("DB", "Creating assignment named " + assignmentName);
        String key = assignmentDb.push().getKey();
        assignmentDb.child(key).setValue
                (new Assignments(assignmentName, dueClass, dueDate, extraInfo, percentComplete));

        /*
        // Test to make sure info is being collected correctly.
        Log.i("Class", dueClass);
        Log.i("Due", dueDate);
        Log.i("Name", homeworkName);
        Log.i("Extra", extraInfo);
        */

        // Bring user back to MainActivity
        startActivity(new Intent(addAssignment.this, MainActivity.class));

        return super.onOptionsItemSelected(item);
    }
}
