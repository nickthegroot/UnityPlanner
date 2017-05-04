package com.nbdeg.unityplanner;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListCourseWorkResponse;
import com.google.api.services.classroom.model.ListCoursesResponse;
import com.google.api.services.classroom.model.ListStudentSubmissionsResponse;
import com.google.api.services.classroom.model.StudentSubmission;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;
import com.nbdeg.unityplanner.utils.AlarmReceiver;
import com.nbdeg.unityplanner.utils.Database;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

@SuppressWarnings({"CanBeFinal", "MismatchedQueryAndUpdateOfCollection"})
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { ClassroomScopes.CLASSROOM_COURSES_READONLY, ClassroomScopes.CLASSROOM_ROSTERS_READONLY, ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY };

    DrawerLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Database.refreshDatabase();

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                this, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        // Sets button to send user to add assignment page when clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof assignmentFragment || currentFragment instanceof dashboardFragment) {
                    startActivity(new Intent(MainActivity.this, addAssignment.class));
                } else if (currentFragment instanceof classFragment){
                    startActivity(new Intent(MainActivity.this, addClass.class));
                }
            }
        });

        mainLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        View headerView = navigationView.getHeaderView(0);
        final TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        final TextView userEmail = (TextView) headerView.findViewById(R.id.user_email);
        final ImageView userPhoto = (ImageView) headerView.findViewById(R.id.user_photo);

        userName.setText(Database.user.getDisplayName());
        if (Database.user.getPhotoUrl() != null) {
            userEmail.setText(Database.user.getEmail());
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
                            userPhoto.setImageResource(R.mipmap.ic_launcher);
                        }
                    });
        }

        // Set Up Notifications
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("firstTime", true)) {
                Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                Calendar hourCal = Calendar.getInstance();
                hourCal.setTimeInMillis(prefs.getLong("notification_time", 90000000));

                calendar.set(Calendar.HOUR_OF_DAY, hourCal.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 1);

                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();
        }

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            dashboardFragment firstFragment = new dashboardFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    /**
     * Changes fragment based on user selected choice from Navigation Bar.
     * @param item ID of selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_assignments) {
            assignmentFragment newFragment = new assignmentFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_classes) {
            classFragment newFragment = new classFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_home) {
            dashboardFragment newFragment = new dashboardFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {
            Intent intent = new AppInviteInvitation.IntentBuilder("Unity Planner")
                    .setMessage("Download Unity Planner to unify your school life today!")
                    .setCallToActionText("Download Now")
                    .build();
            startActivityForResult(intent, 3000);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Adds functions to three dot menu on Main Screens
     * @param item ID of the selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainActivity.this, loginActivity.class));
                                finish();
                            }
                        });
                return super.onOptionsItemSelected(item);

            case R.id.action_sync:
                Database.refreshDatabase();
                if (mCredential.getSelectedAccountName() != null) {
                    new MakeRequestTask(mCredential).execute();
                } else {
                    showSnackbar("Please log into a classroom account in settings first.");
                }
                return super.onOptionsItemSelected(item);

            case R.id.action_tutorial:
                startActivity(new Intent(MainActivity.this, IntroActivity.class));
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * An asynchronous task that handles the Classroom API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, Void> {
        private com.google.api.services.classroom.Classroom mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.classroom.Classroom.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Unity Planner")
                    .build();
        }

        /**
         * Background task to call Classroom API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDataFromApi();
            } catch (Exception e) {
                e.printStackTrace();
                mLastError = e;
                cancel(true);
            }
            return null;
        }

        /**
         * Fetch a list of the names of the first 10 courses the user has access to.
         * @return List course names, or a simple error message if no courses are
         *         found.
         * @throws IOException
         */
        private void getDataFromApi() throws IOException {
            ListCoursesResponse courseResponse = mService.courses().list()
                    .setPageSize(10)
                    .execute();

            ArrayList<String> courseIDs = Database.courseIDs;
            ArrayList<String> courseWorkIDs = Database.courseWorkIDs;

            for (Course course : courseResponse.getCourses()) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                long startDate = System.currentTimeMillis();
                try {
                    startDate = f.parse(course.getCreationTime()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (course.getCourseState().equals("ACTIVE")) {
                    if (!courseIDs.contains(course.getId())) {
                        // Add class to database
                        Database.createClass(new Classes(
                                course.getName(),
                                mService.userProfiles().get(course.getOwnerId()).execute().getName().getFullName(),
                                startDate,
                                course.getRoom(),
                                course.getDescription(),
                                course.getSection(),
                                course
                        ));
                    }

                    // Add assignments to database
                    ListCourseWorkResponse courseworkResponse = mService.courses().courseWork().list(course.getId()).execute();
                    if (courseworkResponse.getCourseWork() != null) {
                        for (CourseWork courseWork : courseworkResponse.getCourseWork()) {
                            if (!courseWorkIDs.contains(courseWork.getId())) {
                                ListStudentSubmissionsResponse studentSubmissionResponse = mService.courses().courseWork().studentSubmissions().list(course.getId(), courseWork.getId()).execute();
                                for (StudentSubmission submission : studentSubmissionResponse.getStudentSubmissions()) {
                                    Calendar cal = Calendar.getInstance();
                                    if (courseWork.getDueDate() != null) {
                                        cal.set(courseWork.getDueDate().getYear(), courseWork.getDueDate().getMonth()-1, courseWork.getDueDate().getDay()-1);
                                    } else {
                                        cal.setTimeInMillis(System.currentTimeMillis());
                                    }
                                    String courseName;
                                    if (Database.changedClassNames.containsKey(course.getName())) {
                                        courseName = Database.changedClassNames.get(course.getName());
                                    } else {
                                        courseName = course.getName();
                                    }
                                    if (submission.getState().equalsIgnoreCase("RETURNED") || submission.getState().equalsIgnoreCase("TURNED_IN")) {
                                        Database.createAssignment(new Assignments(cal.getTimeInMillis(),
                                                courseWork.getTitle(),
                                                courseWork.getDescription(),
                                                courseName,
                                                100,
                                                courseWork));
                                    } else {
                                        Database.createAssignment(new Assignments(cal.getTimeInMillis(),
                                                courseWork.getTitle(),
                                                courseWork.getDescription(),
                                                courseName,
                                                0,
                                                courseWork));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Database.refreshDatabase();
        }

        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                    Dialog dialog = apiAvailability.getErrorDialog(
                            MainActivity.this,
                            ((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode(),
                            1002);
                    dialog.show();
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            classroomLogin.REQUEST_AUTHORIZATION);
                } else {
                    showSnackbar("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                showSnackbar("Request cancelled.");
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}