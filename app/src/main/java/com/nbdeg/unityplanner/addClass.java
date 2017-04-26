package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.nbdeg.unityplanner.data.Classes;
import com.nbdeg.unityplanner.utils.Database;
import com.nbdeg.unityplanner.utils.EditTextDatePicker;

public class addClass extends AppCompatActivity {

    // TODO Add A/B Schedule and select which days class occurs on

    private EditText className;
    private EditText classTeacher;
    private EditText classRoomNumber;
    private EditText classBuildingName;

    private EditTextDatePicker mStartDate;
    private EditTextDatePicker mEndDate;

    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        layout = (RelativeLayout) findViewById(R.id.activity_add_homework);

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
        Classes newClass = new Classes();

        if (className.getText().toString().equals("")) {
            showSnackbar("A class name is needed");
        } else {
            newClass.setName(className.getText().toString());
        }

        newClass.setTeacher(classTeacher.getText().toString());

        newClass.setTeacher(classTeacher.getText().toString());

        if (mStartDate.date != null) {
            newClass.setStartDate(mStartDate.date.getTime());
        }
        if (mEndDate.date != null) {
            newClass.setEndDate(mEndDate.date.getTime());
        }

        newClass.setRoomNumber(classRoomNumber.getText().toString());
        newClass.setBuildingName(classBuildingName.getText().toString());

        Database.createClass(newClass);
        startActivity(new Intent(addClass.this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
