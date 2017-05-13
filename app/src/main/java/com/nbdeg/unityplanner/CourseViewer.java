package com.nbdeg.unityplanner;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Utils.Database;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CourseViewer extends AppCompatActivity {

    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.course_toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.course_toolbar_layout);
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.course_viewer_view);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView viewTeacherName = (TextView) findViewById(R.id.course_viewer_teacher_name);
        TextView viewTime = (TextView) findViewById(R.id.course_viewer_time);
        TextView viewRoomNumber = (TextView) findViewById(R.id.course_viewer_room_number);
        TextView viewDescription = (TextView) findViewById(R.id.course_viewer_description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.course_edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 5/10/2017 create edit course
            }
        });

        String courseID = getIntent().getStringExtra("ID");
        for (Course course : Database.getCourses()) {
            if (course.getID().equals(courseID)) {
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());

                this.course = course;
                layout.setVisibility(View.VISIBLE);

                toolbarLayout.setBackgroundColor(course.getColor());
                toolbarLayout.setTitle(course.getName());

                viewTeacherName.setText(course.getTeacher());

                String date;
                if (course.getTime().getCalEvent() != null) {
                    date = formatter.format(new Date(course.getTime().getStartLong())) + " - " + formatter.format(new Date(course.getTime().getFinish()));
                } else {
                    date = formatter.format(new Date(course.getTime().getStartLong())) + " - " + "???";
                }
                viewTime.setText(date);

                if (course.getRoomNumber() == null || course.getRoomNumber().equals("")) {
                    viewRoomNumber.setText("None");
                } else {
                    viewRoomNumber.setText(course.getRoomNumber());
                }

                if (course.getDescription() == null || course.getDescription().equals("")) {
                    viewDescription.setText("None");
                } else {
                    viewDescription.setText(course.getDescription());
                }

            }
        }


    }
}
