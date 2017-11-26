package com.nbdeg.unityplanner;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignment;
import com.nbdeg.unityplanner.utils.Database;
import com.nbdeg.unityplanner.utils.ListAssignmentAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d", java.util.Locale.getDefault());
    RecyclerView recyTodayView;
    RecyclerView recyTomorrowView;

    ListAssignmentAdapter tomorrowAdapter;
    ListAssignmentAdapter todayAdapter;

    TextView noTodayAssignments;
    TextView noTomorrowAssignments;

    private Paint p = new Paint();

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView)getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);

        getActivity().setTitle("Dashboard");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final TextView todayTitle = (TextView) view.findViewById(R.id.dashboard_today_title);
        final TextView tomorrowTitle = (TextView) view.findViewById(R.id.dashboard_tomorrow_title);
        recyTodayView = (RecyclerView) view.findViewById(R.id.dashboard_today_list);
        recyTomorrowView = (RecyclerView) view.findViewById(R.id.dashboard_tomorrow_list);
        final ImageView todayAdd = (ImageView) view.findViewById(R.id.dashboard_today_add);
        final ImageView tomorrowAdd = (ImageView) view.findViewById(R.id.dashboard_tomorrow_add);

        noTodayAssignments = (TextView) view.findViewById(R.id.dashboard_today_none);
        noTomorrowAssignments = (TextView) view.findViewById(R.id.dashboard_tomorrow_none);

        todayTitle.setText("Due Today - " + formatter.format(new Date()));

        final Calendar tomorrowCal = Calendar.getInstance();
        tomorrowCal.add(Calendar.DATE, 1);
        tomorrowCal.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowCal.set(Calendar.MINUTE, 59);
        tomorrowTitle.setText("Due Tomorrow - " + formatter.format(tomorrowCal.getTime()));

        final Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 23);
        todayCal.set(Calendar.MINUTE, 59);

        final ArrayList<Assignment> todayAssignments = new ArrayList<>();
        final ArrayList<Assignment> tomorrowAssignments = new ArrayList<>();

        recyTodayView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyTomorrowView.setLayoutManager(new LinearLayoutManager(getContext()));

        Database.dueAssignmentDb.orderByChild("dueDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Assignment assignment = userSnapshot.getValue(Assignment.class);

                    if (assignment.getDueDate() < todayCal.getTimeInMillis()) {
                        todayAssignments.add(assignment);
                    } else if (assignment.getDueDate() < tomorrowCal.getTimeInMillis()) {
                        tomorrowAssignments.add(assignment);
                    }
                }

                if (!todayAssignments.isEmpty()) {
                    noTodayAssignments.setVisibility(View.GONE);
                    recyTodayView.setVisibility(View.VISIBLE);

                    todayAdapter = new ListAssignmentAdapter(todayAssignments);
                    recyTodayView.setAdapter(todayAdapter);
                }

                if (!tomorrowAssignments.isEmpty()) {
                    noTomorrowAssignments.setVisibility(View.GONE);
                    recyTomorrowView.setVisibility(View.VISIBLE);

                    tomorrowAdapter = new ListAssignmentAdapter(tomorrowAssignments);
                    recyTomorrowView.setAdapter(tomorrowAdapter);
                }

                initSwipes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        todayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateAssignment.class);
                intent.putExtra("date", todayCal.getTimeInMillis());
                startActivity(intent);
            }
        });

        tomorrowAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateAssignment.class);
                intent.putExtra("date", tomorrowCal.getTimeInMillis());
                startActivity(intent);
            }
        });


        return view;
    }

    private void initSwipes(){
        ItemTouchHelper.SimpleCallback todayTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                todayAdapter.removeAssignment(position, getContext());

                if (todayAdapter.getItemCount() == 0) {
                    noTodayAssignments.setVisibility(View.VISIBLE);
                    recyTodayView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (viewHolder.getAdapterPosition() != -1) {
                        Assignment assignment = todayAdapter.getItem(viewHolder.getAdapterPosition());

                        if (dX > 0) {
                            p.setColor(assignment.getDueCourse().getColor());
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_white);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper.SimpleCallback tomorrowTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                tomorrowAdapter.removeAssignment(position, getContext());
                if (tomorrowAdapter.getItemCount() == 0) {
                    noTomorrowAssignments.setVisibility(View.VISIBLE);
                    recyTomorrowView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (viewHolder.getAdapterPosition() != -1) {
                        Assignment assignment = tomorrowAdapter.getItem(viewHolder.getAdapterPosition());

                        if (dX > 0) {
                            p.setColor(assignment.getDueCourse().getColor());
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_white);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(todayTouchCallback).attachToRecyclerView(recyTodayView);
        new ItemTouchHelper(tomorrowTouchCallback).attachToRecyclerView(recyTomorrowView);
    }
}
