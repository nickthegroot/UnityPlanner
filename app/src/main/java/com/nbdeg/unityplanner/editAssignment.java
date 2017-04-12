package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;
import com.nbdeg.unityplanner.utils.Database;
import com.nbdeg.unityplanner.utils.EditTextDatePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class editAssignment extends AppCompatActivity  {

    private EditText mAssignmentName;
    private EditText mExtraInfo;
    private Spinner mDueClass;
    private ArrayList<String> classListNames = new ArrayList<>();
    private EditTextDatePicker datePicker;
    private SeekBar mPercentComplete;

    Assignments oldAssignment;

    private int percentComplete = 0;

    private String oldAssignmentID;
    private DatabaseReference assignmentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);

        // Find view by ID calls
        mAssignmentName = (EditText) findViewById(R.id.assignment_edit_name);
        mExtraInfo = (EditText) findViewById(R.id.extra_homework_info_edit);
        mDueClass = (Spinner) findViewById(R.id.class_edit_spinner);
        mPercentComplete = (SeekBar) findViewById(R.id.percent_complete_edit);

        // Sets DueDate EditText to open a datepicker when clicked
        datePicker = new EditTextDatePicker(this, R.id.due_date_edit_edittext);
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

        Database.classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classListNames.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Classes mClass = userSnapshot.getValue(Classes.class);
                    classListNames.add(mClass.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(editAssignment.this, R.layout.spinner_layout, classListNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mDueClass.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database", "Error loading classes: " + databaseError.getMessage());
            }
        });

        oldAssignmentID = getIntent().getStringExtra("ID");
        Database.dueAssignmentsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), oldAssignmentID)) {
                        oldAssignment = userSnapshot.getValue(Assignments.class);
                        percentComplete = oldAssignment.getPercentComplete();
                        assignmentReference = userSnapshot.getRef();

                        // Set Existing Data
                        mAssignmentName.setText(oldAssignment.getAssignmentName());
                        mExtraInfo.setText(oldAssignment.getExtraInfo());
                        datePicker.setDisplay(new Date(oldAssignment.getDueDate()));
                        mDueClass.setSelection(classListNames.indexOf(oldAssignment.getDueClass()));
                        mPercentComplete.setProgress(percentComplete);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database", databaseError.getMessage());
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
        Long dueDate = datePicker.date.getTime();
        String assignmentName = mAssignmentName.getText().toString();
        String extraInfo = mExtraInfo.getText().toString();
        String dueClass = mDueClass.getItemAtPosition(mDueClass.getSelectedItemPosition()).toString();

        if (oldAssignment.getPercentComplete() == 100) {
            Assignments newAssignment = oldAssignment;
            // ID Already Set
            newAssignment.setDueDate(dueDate);
            newAssignment.setAssignmentName(assignmentName);
            newAssignment.setExtraInfo(extraInfo);
            newAssignment.setDueClass(dueClass);
            newAssignment.setPercentComplete(percentComplete);
            Database.editAssignment(newAssignment, true);
        } else {
            Assignments newAssignment = oldAssignment;
            // ID Already Set
            newAssignment.setDueDate(dueDate);
            newAssignment.setAssignmentName(assignmentName);
            newAssignment.setExtraInfo(extraInfo);
            newAssignment.setDueClass(dueClass);
            newAssignment.setPercentComplete(percentComplete);
            Database.editAssignment(newAssignment, false);
        }

        // Bring user back to MainActivity
        startActivity(new Intent(editAssignment.this, MainActivity.class));

        return super.onOptionsItemSelected(item);
    }

    public void deleteAssignment(View view) {
        assignmentReference.removeValue();
        startActivity(new Intent(editAssignment.this, MainActivity.class));
    }
}
