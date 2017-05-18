package com.nbdeg.unityplanner.Utils;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nbdeg.unityplanner.CourseViewer;
import com.nbdeg.unityplanner.Data.Time;
import com.nbdeg.unityplanner.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CourseHolder extends RecyclerView.ViewHolder {

    SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());

    TextView courseName;
    TextView courseTeacher;
    TextView courseRoomNumber;
    TextView courseTime;
    View course;

    public CourseHolder(View view) {
        super(view);
        course = view;
        courseName = (TextView)view.findViewById(R.id.course_name);
        courseTeacher = (TextView)view.findViewById(R.id.course_teacher);
        courseRoomNumber = (TextView)view.findViewById(R.id.course_room_number);
        courseTime = (TextView)view.findViewById(R.id.course_time);
    }

    public void setName(String name) {
        courseName.setText(name);
    }

    public void setTeacher(String teacher) {
        courseTeacher.setText(teacher);
    }

    public void setRoomNumber(String roomNumber) {
        courseRoomNumber.setText(roomNumber);
    }

    public void setTime(Time time) {
        String date;
        if (time.getCalEventID() != null) {
            date = formatter.format(new Date(time.getStartLong())) + " - " + formatter.format(new Date(time.getFinish()));
        } else {
            date = formatter.format(new Date(time.getStartLong())) + " - " + "???";
        }
        courseTime.setText(date);
    }

    public void setOnTouch(final String ID) {
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(course.getContext(), CourseViewer.class);
                intent.putExtra("ID", ID);
                course.getContext().startActivity(intent);
            }
        });
    }
}
