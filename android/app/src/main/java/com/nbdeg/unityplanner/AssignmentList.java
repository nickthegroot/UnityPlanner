package com.nbdeg.unityplanner;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.nbdeg.unityplanner.data.Assignment;
import com.nbdeg.unityplanner.utils.AssignmentHolder;
import com.nbdeg.unityplanner.utils.Database;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentList extends Fragment {

    FirebaseRecyclerAdapter mAdapter;
    RecyclerView recyView;

    private Paint p = new Paint();

    public AssignmentList() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_assignment);

        getActivity().setTitle("Assignments");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_assignment_list, container, false);
        recyView = (RecyclerView) mView.findViewById(R.id.assignment_list_view);
        LinearLayoutManager assignmentLayoutManager = new LinearLayoutManager(getContext());

        recyView.setLayoutManager(assignmentLayoutManager);


        Query ref = Database.dueAssignmentDb.orderByChild("dueDate");

        final Calendar dueCal = Calendar.getInstance();
        dueCal.set(Calendar.HOUR_OF_DAY, 0);
        dueCal.set(Calendar.MINUTE, 0);
        dueCal.set(Calendar.SECOND, -1);

        mAdapter = new FirebaseRecyclerAdapter<Assignment, AssignmentHolder>(Assignment.class, R.layout.database_assignment_view, AssignmentHolder.class, ref) {
            @Override
            protected void populateViewHolder(AssignmentHolder viewHolder, Assignment assignment, int position) {
                viewHolder.setOnClick(assignment.getID());
                viewHolder.setName(assignment.getName());
                viewHolder.setDate(new Date(assignment.getDueDate()));
                viewHolder.setCourse(assignment.getDueCourse().getName());
                viewHolder.setColor(assignment.getDueCourse().getColor());
                if (assignment.getDueDate() < dueCal.getTimeInMillis()) {
                    viewHolder.setLate(true);
                } else {
                    viewHolder.setLate(false);
                }
            }
        };

        recyView.setAdapter(mAdapter);
        initSwipe();

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                Assignment oldAssignment = (Assignment) mAdapter.getItem(position);
                Assignment newAssignment = new Assignment(oldAssignment);
                newAssignment.setPercentComplete(100);
                Database.editAssignment(newAssignment, oldAssignment, getContext());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Assignment assignment = (Assignment) mAdapter.getItem(viewHolder.getAdapterPosition());

                    if(dX > 0){
                        p.setColor(assignment.getDueCourse().getColor());
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyView);
    }
}
