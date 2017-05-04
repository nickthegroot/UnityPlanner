package com.nbdeg.unityplanner.AppIntro;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showStatusBar(false);
        showSkipButton(false);
        setFadeAnimation();

        // TODO: 5/3/2017 Add Slides
    }

    /**
     * Creates a new basic slide
     * @param title Large title
     * @param description Small descriptive text
     * @param imageInt Int reference to centered image
     * @param bgColor Background color in int form
     * @return Slide in fragment form
     * @see AppIntroFragment
     */
    private AppIntroFragment newSlide(String title, String description, int imageInt, int bgColor) {
        return AppIntroFragment.newInstance(title, description, imageInt, bgColor);
    }
}
