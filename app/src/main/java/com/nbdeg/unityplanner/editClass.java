package com.nbdeg.unityplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

    private Classes oldClass;
    private DatabaseReference classRef;

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
        final RelativeLayout mEditClassView = (RelativeLayout) findViewById(R.id.edit_class_view);

        final String oldClassID = getIntent().getStringExtra("ID");
        Database.classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), oldClassID)) {

                        // Get data from class
                        oldClass = userSnapshot.getValue(Classes.class);
                        classRef = userSnapshot.getRef();

                        // Set data from class
                        className.setText(oldClass.getName());
                        classTeacher.setText(oldClass.getTeacher());
                        if (oldClass.getStartDate() != null) {
                            mStartDate.setDisplay(new Date(oldClass.getStartDate()));
                        } if (oldClass.getEndDate() != null) {
                            mEndDate.setDisplay(new Date(oldClass.getEndDate()));
                        }
                        classRoomNumber.setText(oldClass.getRoomNumber());
                        classBuildingName.setText(oldClass.getBuildingName());
                        mEditClassView.setVisibility(View.VISIBLE);
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
        if (oldClass != null) {
            Classes newClass = new Classes(
                    oldClass.getName(),
                    oldClass.getTeacher(),
                    oldClass.getStartDate(),
                    oldClass.getEndDate(),
                    oldClass.getRoomNumber(),
                    oldClass.getBuildingName(),
                    oldClass.getID(),
                    oldClass.getDescription(),
                    oldClass.getSection(),
                    oldClass.getClassroomCourse());

            String name = className.getText().toString();
            newClass.setName(name);

            newClass.setTeacher(classTeacher.getText().toString());

            if (mStartDate.date != null) {
                newClass.setStartDate(mStartDate.date.getTime());
            }
            if (mEndDate.date != null) {
                newClass.setEndDate(mEndDate.date.getTime());
            }

            newClass.setRoomNumber(classRoomNumber.getText().toString());
            newClass.setBuildingName(classBuildingName.getText().toString());

            Database.editClass(newClass, oldClass);
            startActivity(new Intent(editClass.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteClass(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        classRef.removeValue();
                        final DatabaseReference allAssignments = Database.allAssignmentsDb;
                        final DatabaseReference dueAssignments = Database.dueAssignmentsDb;
                        final DatabaseReference doneAssignments = Database.doneAssignmentsDb;
                        allAssignments.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                                    if (assignment.getDueClass().equalsIgnoreCase(oldClass.getName())) {
                                        if (assignment.getPercentComplete() == 100) {
                                            doneAssignments.child(assignment.getID()).removeValue();
                                            allAssignments.child(assignment.getID()).removeValue();
                                        } else {
                                            dueAssignments.child(assignment.getID()).removeValue();
                                            allAssignments.child(assignment.getID()).removeValue();
                                        }
                                    }
                                }

                                startActivity(new Intent(editClass.this, MainActivity.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                startActivity(new Intent(editClass.this, MainActivity.class));
                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Deleting a class will delete all " +
                "assignments linked with that class as well.").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }
}
