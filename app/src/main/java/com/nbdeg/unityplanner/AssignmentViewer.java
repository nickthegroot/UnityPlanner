package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Utils.Database;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssignmentViewer extends AppCompatActivity {

    Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.assignment_toolbar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.assignment_toolbar_layout);
        setSupportActionBar(toolbar);

        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.assignment_viewer_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());

        TextView viewCourseName = (TextView) findViewById(R.id.assignment_viewer_course_name);
        TextView viewDueDate = (TextView) findViewById(R.id.assignment_viewer_due_date);
        TextView viewComplete = (TextView) findViewById(R.id.assignment_viewer_complete);
        TextView viewDescription = (TextView) findViewById(R.id.assignment_viewer_description);

        String assignmentID = getIntent().getStringExtra("ID");
        for (Assignment assignment : Database.getAssignments()) {
            if (assignment.getID().equals(assignmentID)) {
                this.assignment = assignment;
                layout.setVisibility(View.VISIBLE);

                toolbarLayout.setBackgroundColor(assignment.getDueCourse().getColor());
                toolbarLayout.setTitle(assignment.getName());

                viewCourseName.setText(assignment.getDueCourse().getName());
                viewDueDate.setText(formatter.format(new Date(assignment.getDueDate())));
                if (assignment.getPercentComplete() == 100) {
                    viewComplete.setText(R.string.assignment_viewer_done);
                } else {
                    viewComplete.setText(R.string.assignment_viewer_not_done);
                }
                if (assignment.getExtraInfo() == null) {
                    viewDescription.setText(R.string.assignment_viewer_none);
                } else if (assignment.getExtraInfo().equals("")) {
                    viewDescription.setText(R.string.assignment_viewer_none);
                } else {
                    viewDescription.setText(assignment.getExtraInfo());
                }
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.assignment_viewer_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssignmentViewer.this, EditAssignment.class);
                intent.putExtra("ID", assignment.getID());
                startActivity(intent);
            }
        });
    }
}
