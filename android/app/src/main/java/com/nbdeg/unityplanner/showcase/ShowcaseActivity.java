package com.nbdeg.unityplanner.showcase;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.nbdeg.unityplanner.R;

public class ShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);

        // Setting the home showcase as the primary fragment
        HomeShowcase homeShow = new HomeShowcase();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.showcase_fragments, homeShow);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
