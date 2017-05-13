package com.nbdeg.unityplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Utils.Database;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseList extends Fragment {

    FirebaseListAdapter mAdapter;

    public CourseList() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_courses);

        getActivity().setTitle("Courses");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_course_list, container, false);
        ListView assignmentView = (ListView) mView.findViewById(R.id.course_list_view);
        DatabaseReference courseDb = Database.courseDb;

        mAdapter = new FirebaseListAdapter<Course>(getActivity(), Course.class, R.layout.database_course_view, courseDb) {
            @Override
            protected void populateView(View view, final Course course, int position) {
                SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
                ((TextView)view.findViewById(R.id.course_name)).setText(course.getName());
                ((TextView)view.findViewById(R.id.course_teacher)).setText(course.getTeacher());
                ((TextView)view.findViewById(R.id.course_room_number)).setText(course.getRoomNumber());

                String date;
                if (course.getTime().getCalEvent() != null) {
                    date = formatter.format(new Date(course.getTime().getStartLong())) + " - " + formatter.format(new Date(course.getTime().getFinish()));
                } else {
                    date = formatter.format(new Date(course.getTime().getStartLong())) + " - " + "???";
                }
                ((TextView)view.findViewById(R.id.course_time)).setText(date);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CourseViewer.class);
                        intent.putExtra("ID", course.getID());
                        startActivity(intent);
                    }
                });
            }
        };
        assignmentView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
