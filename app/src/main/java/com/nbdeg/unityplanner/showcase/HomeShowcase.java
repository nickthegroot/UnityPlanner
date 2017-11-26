package com.nbdeg.unityplanner.showcase;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class HomeShowcase extends Fragment {


    public HomeShowcase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_showcase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new MaterialShowcaseView.Builder(getActivity())
                .setTitleText("Welcome to Unity Planner")
                .setContentText("The app to unify your school life")
                .setDismissText("Continue")
                .setTarget(view.findViewById(R.id.showcase_home_layout))
                .withRectangleShape()
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        CourseShowcase courseShow = new CourseShowcase();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.showcase_fragments, courseShow);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }
                })
                .show();
    }
}
