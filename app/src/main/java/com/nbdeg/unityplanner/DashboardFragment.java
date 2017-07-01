package com.nbdeg.unityplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignment;
import com.nbdeg.unityplanner.utils.Database;
import com.nbdeg.unityplanner.utils.TodayAssignmentHolder;
import com.nbdeg.unityplanner.utils.TomorrowAssignmentHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d", java.util.Locale.getDefault());

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);

        getActivity().setTitle("Dashboard");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final TextView todayTitle = view.findViewById(R.id.dashboard_today_title);
        final TextView tomorrowTitle = view.findViewById(R.id.dashboard_tomorrow_title);
        final ListView todayList = view.findViewById(R.id.dashboard_today_list);
        final ListView tomorrowList = view.findViewById(R.id.dashboard_tomorrow_list);
        final ImageView todayAdd = view.findViewById(R.id.dashboard_today_add);
        final ImageView tomorrowAdd = view.findViewById(R.id.dashboard_tomorrow_add);

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
                    view.findViewById(R.id.dashboard_today_none).setVisibility(View.GONE);
                    todayList.setVisibility(View.VISIBLE);

                    ArrayAdapter<Assignment> todayAdapter = new TodayAssignmentHolder(getContext(), todayAssignments);
                    todayList.setAdapter(todayAdapter);
                }

                if (!tomorrowAssignments.isEmpty()) {
                    view.findViewById(R.id.dashboard_tomorrow_none).setVisibility(View.GONE);
                    tomorrowList.setVisibility(View.VISIBLE);

                    ArrayAdapter<Assignment> tomorrowAdapter = new TomorrowAssignmentHolder(getContext(), tomorrowAssignments);
                    tomorrowList.setAdapter(tomorrowAdapter);
                }
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
}
