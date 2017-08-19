package com.nbdeg.unityplanner.showcase;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nbdeg.unityplanner.Dashboard;
import com.nbdeg.unityplanner.R;

import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncShowcase extends Fragment {


    public SyncShowcase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sync_showcase, container, false);

        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(view.findViewById(R.id.showcase_googleclassroom))
                .setTitleText("Syncing with Google Classroom")
                .setContentText(
                        "Unity Planner can pull assignments and courses from Google Classroom, meaning you can spend more time studying." +
                                " Give it a try in the navigation bar.")
                .setDismissText("Ready? Let's go.")
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        Intent i = new Intent(getActivity(), Dashboard.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                })
                .show();

        // Inflate the layout for this fragment
        return view;
    }

}
