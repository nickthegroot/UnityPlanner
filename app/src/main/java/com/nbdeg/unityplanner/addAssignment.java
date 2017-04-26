package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class addAssignment extends AppCompatActivity  {

    private RelativeLayout layout;
    private EditText mAssignmentName;
    private EditText mExtraInfo;
    private Spinner mDueClass;
    private int percentComplete = 0;
    private ArrayList<String> classListNames = new ArrayList<>();
    private EditTextDatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        // Find view by ID calls
        layout = (RelativeLayout) findViewById(R.id.activity_add_homework);
        mAssignmentName = (EditText) findViewById(R.id.assignment_name);
        mExtraInfo = (EditText) findViewById(R.id.extra_homework_info);
        mDueClass = (Spinner) findViewById(R.id.class_spinner);
        SeekBar mPercentComplete = (SeekBar) findViewById(R.id.percentComplete);

        // Sets DueDate EditText to open a datepicker when clicked
        datePicker = new EditTextDatePicker(this, R.id.due_date_edittext);

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

        DatabaseReference classDb = Database.classDb;
        classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classListNames.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Classes mClass = userSnapshot.getValue(Classes.class);
                    classListNames.add(mClass.getName());
                    Log.i("Database", "Class loaded: " + mClass.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(addAssignment.this, R.layout.spinner_layout, classListNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mDueClass.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database", "Error loading classes: " + databaseError.getMessage());
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
        Boolean isValidAssignment = true;

        // Getting information from views
        Long dueDate = datePicker.date.getTime();
        String assignmentName = mAssignmentName.getText().toString();
        String extraInfo = mExtraInfo.getText().toString();

        Assignments newAssignment = new Assignments();

        if (datePicker.date != null) {
            newAssignment.setDueDate(datePicker.date.getTime());
        } else {
            isValidAssignment = false;
        }

        if (!mAssignmentName.getText().toString().equals("")) {
            newAssignment.setAssignmentName(mAssignmentName.getText().toString());
        } else {
            isValidAssignment = false;
        }

        newAssignment.setExtraInfo(mExtraInfo.getText().toString());
        newAssignment.setDueClass(mDueClass.getItemAtPosition(mDueClass.getSelectedItemPosition()).toString());

        if (isValidAssignment) {
            // Bring user back to MainActivity
            Database.createAssignment(newAssignment);
            startActivity(new Intent(addAssignment.this, MainActivity.class));
        }
        else {
            Snackbar.make(layout, "Some essential fields are not filled out!", Snackbar.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
