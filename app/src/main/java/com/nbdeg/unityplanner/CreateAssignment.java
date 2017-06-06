package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Utils.Database;
import com.nbdeg.unityplanner.Utils.EditTextDatePicker;

import java.util.ArrayList;
import java.util.Date;

public class CreateAssignment extends AppCompatActivity {

    ArrayList<Course> courses = new ArrayList<>();
    EditText viewName;
    EditText viewExtra;
    EditTextDatePicker viewDate;
    Spinner viewCourse;
    CheckBox viewCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);

        if (Database.getUser() == null) {
            startActivity(new Intent(CreateAssignment.this, LauncherLogin.class));
        }

        viewName = (EditText) findViewById(R.id.assignment_create_name);
        viewExtra = (EditText) findViewById(R.id.assignment_create_extra);
        viewCourse = (Spinner) findViewById(R.id.assignment_create_course);
        viewCompleted = (CheckBox) findViewById(R.id.assignment_create_completed);
        viewDate = new EditTextDatePicker(this, R.id.assignment_create_date);

        if (getIntent().getLongExtra("date", 0) != 0) {
            viewDate.setDisplay(new Date(getIntent().getLongExtra("date", 0)));
        }

        final ArrayList<String> courseNames = new ArrayList<>();
        Database.courseDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Course course = userSnapshot.getValue(Course.class);
                    courses.add(course);
                    courseNames.add(course.getName());
                }

                if (courseNames.isEmpty()) {
                    courseNames.add("Please create a course");
                }

                ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(CreateAssignment.this,
                        android.R.layout.simple_spinner_item, courseNames);

                viewCourse.setAdapter(courseAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Assignment assignment = new Assignment();

        // Checking if assignment name is valid
        if (viewName.getText().toString().equals("")) {
            Toast.makeText(this, "An assignment name is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            assignment.setName(viewName.getText().toString());
        }

        // Checking if assignment date is valid
        if (viewDate.date == null) {
            Toast.makeText(this, "An assignment date is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            assignment.setDueDate(viewDate.date.getTime());
        }

        // Checking if assignment course is valid
        if (viewCourse.getSelectedItem().toString().equals("Please create a course")) {
            Toast.makeText(this, "Please create a course first", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            for (Course course : courses) {
                if (course.getName().equals(viewCourse.getSelectedItem().toString())) {
                    assignment.setDueCourse(course);
                }
            }
        }

        // Checking if assignment is complected
        if (viewCompleted.isChecked()) {
            assignment.setPercentComplete(100);
        } else {
            assignment.setPercentComplete(0);
        }

        // Setting any extra info
        assignment.setExtraInfo(viewExtra.getText().toString());

        // Creating assignment if all went well
        Database.createAssignment(assignment, getApplicationContext());
        FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("assignment_created", null);
        startActivity(new Intent(CreateAssignment.this, Dashboard.class));

        return super.onOptionsItemSelected(item);
    }
}
