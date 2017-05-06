package com.nbdeg.unityplanner;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Utils.Database;

public class AssignmentViewer extends AppCompatActivity {

    Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.assignment_viewer_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.assignment_edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String assignmentID = getIntent().getStringExtra("ID");
        for (Assignment assignment : Database.assignments) {
            if (assignment.getID().equals(assignmentID)) {
                this.assignment = assignment;
                layout.setVisibility(View.VISIBLE);

                // TODO: 5/4/2017 Set all assignment info here
                toolbar.setBackgroundColor(assignment.getDueCourse().getColor());
                toolbar.setTitle(assignment.getName());
            }
        }
    }
}
