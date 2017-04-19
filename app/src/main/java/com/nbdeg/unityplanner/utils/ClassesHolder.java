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
    public final View mView;

    public ClassesHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.view_class_name);
        mTeacherField = (TextView) itemView.findViewById(R.id.view_class_teacher);
        mRoomNumField = (TextView) itemView.findViewById(R.id.view_class_room);
        mView = itemView;
    }

    public void setEverything(Classes mClass) {
        if (mClass.getName() != null) {
            mNameField.setText(mClass.getName());
        }
        if (mClass.getTeacher() != null) {
            mTeacherField.setText(mClass.getTeacher());
        }
        if (mClass.getRoomNumber() != null) {
            mRoomNumField.setText(mClass.getRoomNumber());
        }
    }
}
