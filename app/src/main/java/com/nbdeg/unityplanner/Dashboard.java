package com.nbdeg.unityplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nbdeg.unityplanner.Utils.Database;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int FragmentLayoutID;
    int REQUEST_INVITE = 4001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Adding Dashboard Fragment as primary fragment
        FragmentLayoutID = R.id.dashboard_fragments;
        DashboardFragment dashFrag = new DashboardFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(FragmentLayoutID, dashFrag);
        transaction.addToBackStack(null);

        transaction.commit();

        // Adding Name and E-Mail to Nav
        View hView =  navigationView.getHeaderView(0);
        final ImageView userPhoto = (ImageView) hView.findViewById(R.id.nav_header_photo);
        final TextView userName = (TextView) hView.findViewById(R.id.nav_header_user);
        final TextView userEmail = (TextView) hView.findViewById(R.id.nav_header_email);

        if (Database.user.getDisplayName() != null) {
            userName.setText(Database.user.getDisplayName());
        }
        if (Database.user.getEmail() != null) {
            userEmail.setText(Database.user.getEmail());
        }
        if (Database.user.getPhotoUrl() != null) {
            Picasso.with(this).load(Database.user.getPhotoUrl())
                    .resize(150, 150)
                    .into(userPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            userPhoto.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError() {
                            userPhoto.setImageResource(R.mipmap.ic_launcher_round);
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Database.refreshDatabase();
                            startActivity(new Intent(Dashboard.this, LauncherLogin.class));
                            finish();
                        }
                    });
        } else if (id == R.id.action_intro) {
            // TODO: 5/3/2017 Start App Intro
            Toast.makeText(this, "Work In Progress", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            DashboardFragment dashFrag = new DashboardFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(FragmentLayoutID, dashFrag);
            transaction.addToBackStack(null);

            transaction.commit();
        } else if (id == R.id.nav_assignment) {
            AssignmentList assignmentFrag = new AssignmentList();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(FragmentLayoutID, assignmentFrag);
            transaction.addToBackStack(null);

            transaction.commit();
        } else if (id == R.id.nav_courses) {
            CourseList courseFrag = new CourseList();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(FragmentLayoutID, courseFrag);
            transaction.addToBackStack(null);

            transaction.commit();
        } else if (id == R.id.nav_share) {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.app_name))
                    .setMessage(getString(R.string.invitation_message))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();
            startActivityForResult(intent, REQUEST_INVITE);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(Dashboard.this, Settings.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
