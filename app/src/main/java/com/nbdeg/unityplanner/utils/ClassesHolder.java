package com.nbdeg.unityplanner.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Classes;

public class ClassesHolder extends RecyclerView.ViewHolder {
    private final TextView mNameField;
    private final TextView mTeacherField;
    private final TextView mRoomNumField;

    public ClassesHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.view_class_name);
        mTeacherField = (TextView) itemView.findViewById(R.id.view_class_teacher);
        mRoomNumField = (TextView) itemView.findViewById(R.id.view_class_room);
    }

    public void setEverything(Classes mClass) {
        mNameField.setText(mClass.getName());
        mTeacherField.setText(mClass.getTeacher());
        mRoomNumField.setText(String.valueOf(mClass.getRoomNumber()));
    }

    public void setName(String name) {
        mNameField.setText(name);
    }
    public void setTeacher(String teacher) {
        mTeacherField.setText(teacher);
    }
    public void setRoomNum(int roomNum) {
        mRoomNumField.setText(String.valueOf(roomNum));
    }
}
