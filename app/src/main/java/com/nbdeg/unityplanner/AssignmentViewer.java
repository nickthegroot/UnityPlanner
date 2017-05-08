package com.nbdeg.unityplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Utils.Database;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

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

        String assignmentID = getIntent().getStringExtra("ID");
        for (Assignment assignment : Database.assignments) {
            if (assignment.getID().equals(assignmentID)) {
                this.assignment = assignment;
                layout.setVisibility(View.VISIBLE);

                // TODO: 5/4/2017 Set all assignment info here
                toolbarLayout.setBackgroundColor(assignment.getDueCourse().getColor());
                toolbarLayout.setTitle(assignment.getName());
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
