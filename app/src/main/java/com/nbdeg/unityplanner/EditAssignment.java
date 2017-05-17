package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Utils.Database;
import com.nbdeg.unityplanner.Utils.EditTextDatePicker;

import java.util.ArrayList;
import java.util.Date;

public class EditAssignment extends AppCompatActivity {

    Assignment oldAssignment;

    EditText viewName;
    EditText viewExtra;
    EditTextDatePicker viewDate;
    Spinner viewCourse;
    CheckBox viewCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);

        RelativeLayout viewLayout = (RelativeLayout) findViewById(R.id.assignment_edit_view);
        Button viewDelete = (Button) findViewById(R.id.assignment_edit_delete);
        viewName = (EditText) findViewById(R.id.assignment_edit_name);
        viewExtra = (EditText) findViewById(R.id.assignment_edit_extra);
        viewCourse = (Spinner) findViewById(R.id.assignment_edit_course);
        viewCompleted = (CheckBox) findViewById(R.id.assignment_edit_completed);
        viewDate = new EditTextDatePicker(this, R.id.assignment_edit_date);

        ArrayList<String> courseNames = new ArrayList<>();
        for (Course course : Database.getCourses()) {
            courseNames.add(course.getName());
        }
        if (courseNames.isEmpty()) {
            courseNames.add("Please create a course");
        }
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, courseNames);

        viewCourse.setAdapter(courseAdapter);


        /* Setting values to previous assignment */
        String ID = getIntent().getStringExtra("ID");
        for (Assignment assignment : Database.getAssignments()) {
            if (assignment.getID().equals(ID)) {
                oldAssignment = assignment;

                viewName.setText(assignment.getName());
                viewExtra.setText(assignment.getExtraInfo());
                viewDate.setDisplay(new Date(assignment.getDueDate()));
                viewCourse.setSelection(courseAdapter.getPosition(assignment.getDueCourse().getName()));
                if (assignment.getPercentComplete() == 100) {
                    viewCompleted.setChecked(true);
                } else {
                    viewCompleted.setChecked(false);
                }

                viewLayout.setVisibility(View.VISIBLE);

                viewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Database.deleteAssignment(oldAssignment);
                        startActivity(new Intent(EditAssignment.this, Dashboard.class));
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Assignment newAssignment = new Assignment();
        // Setting this assignment with old assignment values (which will be updated if changed)
        newAssignment.setName(oldAssignment.getName());
        newAssignment.setDueCourse(oldAssignment.getDueCourse());
        newAssignment.setPercentComplete(oldAssignment.getPercentComplete());
        newAssignment.setExtraInfo(oldAssignment.getExtraInfo());
        newAssignment.setID(oldAssignment.getID());

        if (oldAssignment.getClassroomCourseWork() != null) {
            newAssignment.setClassroomCourseWork(oldAssignment.getClassroomCourseWork());
        }

        // Checking if assignment name is valid
        if (viewName.getText().toString().equals("")) {
            Toast.makeText(this, "An assignment name is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            newAssignment.setName(viewName.getText().toString());
        }

        // Checking if assignment date is valid
        if (viewDate.date == null) {
            Toast.makeText(this, "An assignment date is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
             newAssignment.setDueDate(viewDate.date.getTime());
        }

        // Checking if assignment course is valid
        if (viewCourse.getSelectedItem().toString().equals("Please create a course")) {
            Toast.makeText(this, "Please create a course first", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            for (Course course : Database.getCourses()) {
                if (course.getName().equals(viewCourse.getSelectedItem().toString())) {
                    newAssignment.setDueCourse(course);
                }
            }
        }

        // Checking if assignment is complected
        if (viewCompleted.isChecked()) {
            newAssignment.setPercentComplete(100);
        } else {
            newAssignment.setPercentComplete(0);
        }

        // Setting any extra info
        newAssignment.setExtraInfo(viewExtra.getText().toString());

        Database.editAssignment(newAssignment, oldAssignment);
        startActivity(new Intent(EditAssignment.this, Dashboard.class));

        return super.onOptionsItemSelected(item);
    }
}
