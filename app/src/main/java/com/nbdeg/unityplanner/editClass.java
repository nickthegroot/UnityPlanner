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
    private int roomNumber;
    private String buildingName;
    private DatabaseReference classRef;

    Database db = new Database();

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
        db.classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.equals(userSnapshot.getKey(), oldClassID)) {

                        // Get data from class
                        Classes mClass = userSnapshot.getValue(Classes.class);
                        name = mClass.getName();
                        teacher = mClass.getTeacher();
                        startDate = new Date(mClass.getStartDate());
                        endDate = new Date(mClass.getEndDate());
                        roomNumber = mClass.getRoomNumber();
                        buildingName = mClass.getBuildingName();
                        classRef = userSnapshot.getRef();

                        // Set data from class
                        className.setText(name);
                        classTeacher.setText(teacher);
                        mStartDate.setDisplay(startDate);
                        mEndDate.setDisplay(endDate);
                        classRoomNumber.setText(Integer.toString(roomNumber));
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
        if (!classRoomNumber.getText().toString().isEmpty()) {
            roomNumber = Integer.parseInt(classRoomNumber.getText().toString());
        }
        String buildingName = classBuildingName.getText().toString();

        db.editClass(oldClassID, new Classes(name, teacherName, startDate, endDate, roomNumber, buildingName, oldClassID));
        startActivity(new Intent(editClass.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void deleteClass(View view) {
        classRef.removeValue();
        startActivity(new Intent(editClass.this, MainActivity.class));
    }
}
