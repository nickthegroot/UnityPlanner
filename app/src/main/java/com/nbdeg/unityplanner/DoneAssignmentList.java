package com.nbdeg.unityplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.Utils.AssignmentHolder;
import com.nbdeg.unityplanner.Utils.Database;

import java.util.Date;

public class DoneAssignmentList extends AppCompatActivity {

    FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_assignment_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView assignmentView = (RecyclerView) findViewById(R.id.old_assignment_list_view);
        LinearLayoutManager assignmentLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assignmentView.getContext(),
                assignmentLayoutManager.getOrientation());

        assignmentView.addItemDecoration(dividerItemDecoration);
        assignmentView.setLayoutManager(assignmentLayoutManager);


        Query ref = Database.doneAssignmentDb.orderByChild("dueDate");

        mAdapter = new FirebaseRecyclerAdapter<Assignment, AssignmentHolder>(Assignment.class, R.layout.database_assignment_view, AssignmentHolder.class, ref) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, Assignment assignment, int position) {
                viewHolder.setName(assignment.getName());
                viewHolder.setDate(new Date(assignment.getDueDate()));
                viewHolder.setCourse(assignment.getDueCourse().getName());
            }
        };

        assignmentView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }


}
