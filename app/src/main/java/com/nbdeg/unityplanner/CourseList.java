package com.nbdeg.unityplanner;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Utils.CourseHolder;
import com.nbdeg.unityplanner.Utils.Database;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseList extends Fragment {

    FirebaseRecyclerAdapter mAdapter;

    public CourseList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        RecyclerView courseView = (RecyclerView) mView.findViewById(R.id.course_list_view);
        LinearLayoutManager courseLayoutManager = new LinearLayoutManager(getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(courseView.getContext(),
                courseLayoutManager.getOrientation());

        courseView.addItemDecoration(dividerItemDecoration);
        courseView.setLayoutManager(courseLayoutManager);

        DatabaseReference courseDb = Database.courseDb;

        mAdapter = new FirebaseRecyclerAdapter<Course, CourseHolder>(Course.class, R.layout.database_course_view, CourseHolder.class, courseDb) {
            @Override
            protected void populateViewHolder(CourseHolder viewHolder, Course course, int position) {
                viewHolder.setName(course.getName());
                viewHolder.setTeacher(course.getTeacher());
                viewHolder.setRoomNumber(course.getRoomNumber());
                viewHolder.setTime(course.getTime());
                viewHolder.setOnTouch(course.getID());
                viewHolder.setColor(course.getColor());
            }
        };

        courseView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
