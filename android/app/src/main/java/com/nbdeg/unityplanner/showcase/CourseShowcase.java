package com.nbdeg.unityplanner.showcase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nbdeg.unityplanner.R;

import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseShowcase extends Fragment {


    public CourseShowcase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_showcase, container, false);

        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(view.findViewById(R.id.showcase_course))
                .setTitleText("Courses")
                .setContentText("In order to add assignments, you have to have classes. Use this button to add all of your classes")
                .setDismissText("Continue")
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        AssignmentShowcase showFrag = new AssignmentShowcase();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.dashboard_fragments, showFrag);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }
                })
                .show();

        return view;
    }

}
