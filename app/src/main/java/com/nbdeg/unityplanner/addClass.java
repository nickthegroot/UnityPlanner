package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.nbdeg.unityplanner.data.Classes;
import com.nbdeg.unityplanner.utils.Database;
import com.nbdeg.unityplanner.utils.EditTextDatePicker;

public class addClass extends AppCompatActivity {

    // TODO Add A/B Schedule
    // TODO Select which days class occurs on

    private EditText className;
    private EditText classTeacher;
    private EditText classRoomNumber;
    private EditText classBuildingName;

    private EditTextDatePicker mStartDate;
    private EditTextDatePicker mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Setting date pickers to have have pop-up calendars
        mStartDate = new EditTextDatePicker(this, R.id.class_start_date);
        mEndDate = new EditTextDatePicker(this, R.id.class_end_date);

        // Setting EditText Variables
        className = (EditText) findViewById(R.id.class_name);
        classTeacher = (EditText) findViewById(R.id.class_teacher);
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
        Long startDate = mStartDate.date.getTime();
        Long endDate = mEndDate.date.getTime();
        String roomNumber = classRoomNumber.getText().toString();
        String buildingName = classBuildingName.getText().toString();

        Database database = new Database();
        database.addClass(new Classes(name, teacherName, startDate, endDate, roomNumber, buildingName));
        startActivity(new Intent(addClass.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
