package com.nbdeg.unityplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbdeg.unityplanner.data.Classes;

public class addClass extends AppCompatActivity {

    // TODO Add A/B Schedule
    // TODO Select which days class occurs on

    private DatabaseReference classDb;

    private EditText className;
    private EditText classTeacher;
    private EditText classStartDate;
    private EditText classEndDate;
    private EditText classRoomNumber;
    private EditText classBuildingName;

    private int roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Setting date pickers to have have pop-up calendars
        new EditTextDatePicker(this, R.id.class_start_date);
        new EditTextDatePicker(this, R.id.class_end_date);

        // Setting EditText Variables
        className = (EditText) findViewById(R.id.class_name);
        classTeacher = (EditText) findViewById(R.id.class_teacher);
        classStartDate = (EditText) findViewById(R.id.class_start_date);
        classEndDate = (EditText) findViewById(R.id.class_end_date);
        classRoomNumber = (EditText) findViewById(R.id.class_room);
        classBuildingName = (EditText) findViewById(R.id.class_building);
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
        String startDate = classStartDate.getText().toString();
        String endDate = classEndDate.getText().toString();
        if (!classRoomNumber.getText().toString().isEmpty()) {
            roomNumber = Integer.parseInt(classRoomNumber.getText().toString());
        }
        String buildingName = classBuildingName.getText().toString();

        database db = new database();
        db.addClass(new Classes(name, teacherName, startDate, endDate, roomNumber, buildingName));
        startActivity(new Intent(addClass.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
