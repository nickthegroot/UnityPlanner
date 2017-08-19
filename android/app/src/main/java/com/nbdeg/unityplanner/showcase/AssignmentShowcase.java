package com.nbdeg.unityplanner.showcase;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Assignment;
import com.nbdeg.unityplanner.data.Course;
import com.nbdeg.unityplanner.data.Time;
import com.nbdeg.unityplanner.utils.TomorrowAssignmentHolder;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * A showcase {@link Fragment} for assignments.
 */
public class AssignmentShowcase extends Fragment {


    public AssignmentShowcase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_assignment_showcase, container, false);

        Calendar oneMonth = Calendar.getInstance();
        oneMonth.add(Calendar.MONTH, 1);

        Course english = new Course(
                "Sophomore English",
                "Mr. Dennis",
                new Time(true, true, false, false, null, System.currentTimeMillis(), (long) oneMonth.get(Calendar.HOUR_OF_DAY) * 3600000, oneMonth.getTimeInMillis()),
                "203",
                "Exploring how literature shapes and reflect our collective culture through the study of myths, archetypes, plays, and novels.",
                null,
                Color.BLUE
        );

        Assignment bookWork = new Assignment(
                "Read Chapters 2-5 in To Kill a Mockingbird",
                System.currentTimeMillis(),
                english,
                0,
                "",
                null
        );

        Assignment essay = new Assignment(
                "Finish Essay Rough Draft",
                System.currentTimeMillis(),
                english,
                0,
                "",
                null
        );
        // Time(BLOCK, ADAY, BDAY, DAY, DAYS, START TIMe, END TIME, END DATE)
        // Course(NAME, TEACHER, TIME, ROOMNUM, DESCRIPTION, CLASSROOM, COLOR)
        // Assignment("NAME", DUEDATE, DUECOURSE, PERCENT COMPLETE, EXTRA INFO)

        // Add example assignments

        ArrayList<Assignment> assignments = new ArrayList<>(2);
        assignments.add(bookWork);
        assignments.add(essay);

        TomorrowAssignmentHolder holder = new TomorrowAssignmentHolder(getContext(), assignments);
        ((ListView)view.findViewById(R.id.showcase_assignment_list_view)).setAdapter(holder);

        new MaterialShowcaseView.Builder(getActivity())
            .setTarget(view.findViewById(R.id.showcase_assignment))
                .withRectangleShape()
            .setTitleText("Assignments")
                .setContentText("Assignments are at the core of Unity Planner, allowing you to get reminders and see what's coming up.")
                .setDismissText("Continue")
            .setListener(new IShowcaseListener() {
                @Override
                public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {
                }

                @Override
                public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                    SyncShowcase showFrag = new SyncShowcase();

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.showcase_fragments, showFrag);
                    transaction.addToBackStack(null);

                    transaction.commit();
                }
            })
            .show();

        // Inflate the layout for this fragment
        return view;
    }

}
