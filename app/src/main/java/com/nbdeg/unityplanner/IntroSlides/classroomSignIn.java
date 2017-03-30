package com.nbdeg.unityplanner.IntroSlides;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.classroomLogin;

public class classroomSignIn extends Fragment implements ISlideBackgroundColorHolder {
    private LinearLayout classroomView;
    private Button classroomSignIn;

    public classroomSignIn() {
        // Required empty public constructor
    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#00796B");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        if (classroomView != null) {
            classroomView.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classroom_sign_in, container, false);
        classroomView = (LinearLayout) view.findViewById(R.id.gclassroom_view);
        classroomSignIn = (Button) view.findViewById(R.id.gclassroom_signin);

        classroomSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), classroomLogin.class));
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}