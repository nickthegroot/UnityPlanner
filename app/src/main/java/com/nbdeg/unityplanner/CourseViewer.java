package com.nbdeg.unityplanner;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Utils.Database;

public class CourseViewer extends AppCompatActivity {

    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.course_viewer_view);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.course_edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String courseID = getIntent().getStringExtra("ID");
        for (Course course : Database.courses) {
            if (course.getID().equals(courseID)) {
                this.course = course;
                layout.setVisibility(View.VISIBLE);

                // TODO: 5/4/2017 Set all course info here
                toolbar.setBackgroundColor(course.getColor());
                toolbar.setTitle(course.getName());
            }
        }


    }
}
