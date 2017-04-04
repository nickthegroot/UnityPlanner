package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;
import com.nbdeg.unityplanner.utils.Database;
import com.nbdeg.unityplanner.utils.EditTextDatePicker;

import java.util.Date;
import java.util.Objects;

public class editClass extends AppCompatActivity {

    // TODO Add A/B Schedule
    // TODO Select which days class occurs on

    private EditText className;
    private EditText classTeacher;
    private EditText classRoomNumber;
    private EditText classBuildingName;

    private EditTextDatePicker mStartDate;
    private EditTextDatePicker mEndDate;

    private String oldClassID;
    private String name;
    private String teacher;
    private Date startDate;
    private Date endDate;
    private String roomNumber;
    private String buildingName;
    private DatabaseReference classRef;

    private Classes oldClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        // Setting date pickers to have have pop-up calendars
        mStartDate = new EditTextDatePicker(this, R.id.class_edit_start_date);
        mEndDate = new EditTextDatePicker(this, R.id.class_edit_end_date);

        // Setting EditText Variables
        className = (EditText) findViewById(R.id.class_edit_name);
        classTeacher = (EditText) findViewById(R.id.class_edit_teacher);
        classRoomNumber = (EditText) findViewById(R.id.class_edit_room);
        classBuildingName = (EditText) findViewById(R.id.class_edit_building);

        oldClassID = getIntent().getStringExtra("ID");
        Database.classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), oldClassID)) {

                        // Get data from class
                        oldClass = userSnapshot.getValue(Classes.class);
                        name = oldClass.getName();
                        teacher = oldClass.getTeacher();
                        if (oldClass.getStartDate() != null) {
                            startDate = new Date(oldClass.getStartDate());
                        } if (oldClass.getEndDate() != null) {
                            endDate = new Date(oldClass.getEndDate());
                        }
                        roomNumber = oldClass.getRoomNumber();
                        buildingName = oldClass.getBuildingName();
                        classRef = userSnapshot.getRef();

                        // Set data from class
                        className.setText(name);
                        classTeacher.setText(teacher);
                        if (startDate != null) {
                            mStartDate.setDisplay(startDate);
                        } if (endDate != null) {
                            mEndDate.setDisplay(endDate);
                        }
                        classRoomNumber.setText(roomNumber);
                        classBuildingName.setText(buildingName);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = className.getText().toString();
        String teacherName = classTeacher.getText().toString();
        Long startDate = mStartDate.date.getTime();
        Long endDate = mEndDate.date.getTime();
        String roomNumber = classRoomNumber.getText().toString();
        String buildingName = classBuildingName.getText().toString();

        Database.editClass(oldClassID, new Classes(name, teacherName, startDate, endDate, roomNumber, buildingName, oldClassID));
        startActivity(new Intent(editClass.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void deleteClass(View view) {
        classRef.removeValue();
        final DatabaseReference allAssignments = Database.allAssignmentsDb;
        final DatabaseReference dueAssignments = Database.dueAssignmentsDb;
        final DatabaseReference doneAssignments = Database.doneAssignmentsDb;
        allAssignments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                        if (assignment.getPercent() == 100) {
                            doneAssignments.child(assignment.getID()).removeValue();
                            allAssignments.child(assignment.getID()).removeValue();
                        } else {
                            dueAssignments.child(assignment.getID()).removeValue();
                            allAssignments.child(assignment.getID()).removeValue();
                    }
                }

                startActivity(new Intent(editClass.this, MainActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                startActivity(new Intent(editClass.this, MainActivity.class));
            }
        });

    }
}
