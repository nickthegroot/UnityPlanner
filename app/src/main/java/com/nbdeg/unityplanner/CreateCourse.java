package com.nbdeg.unityplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseException;
import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Data.Time;
import com.nbdeg.unityplanner.Utils.CourseAddTime;
import com.nbdeg.unityplanner.Utils.Database;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.nbdeg.unityplanner.Dashboard.REQUEST_PERMISSION_GET_ACCOUNTS;

public class CreateCourse extends AppCompatActivity implements SimpleDialog.OnDialogResultListener {

    private static final String TAG = "CreateCourse";
    private static final int COURSE_GET_TIME = 1050;
    private GoogleAccountCredential gCredential;
    private static com.google.api.services.calendar.Calendar calService;
    private static Course course;
    private Time time;

    private View viewColor;
    private Button viewAddTime;
    private TextView viewCourseTime;
    private EditText viewCourseName;
    private EditText viewCourseTeacher;
    private EditText viewCourseRoomNumber;
    private EditText viewCourseDescription;

    final static private String COLOR_DIALOG = "colorDialog";
    static private int courseColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        Button colorSelector = (Button) findViewById(R.id.course_create_button);
        viewAddTime = (Button) findViewById(R.id.course_create_add_time);
        viewCourseTime = (TextView) findViewById(R.id.course_create_time);
        viewColor = findViewById(R.id.course_create_view_color);
        viewCourseName = (EditText) findViewById(R.id.course_create_name);
        viewCourseTeacher = (EditText) findViewById(R.id.course_create_teacher);
        viewCourseRoomNumber = (EditText) findViewById(R.id.course_create_room_number);
        viewCourseDescription = (EditText) findViewById(R.id.course_create_description);

        courseColor = getMatColor("200");
        viewColor.setBackgroundColor(courseColor);

        colorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleColorDialog.build()
                        .title("Pick a course color")
                        .allowCustom(true)
                        .show(CreateCourse.this, COLOR_DIALOG);
            }
        });

        viewCourseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCourse.this, CourseAddTime.class);
                startActivityForResult(intent, COURSE_GET_TIME);
            }
        });

        viewAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCourse.this, CourseAddTime.class);
                startActivityForResult(intent, COURSE_GET_TIME);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: Activity Result");

        if (requestCode == COURSE_GET_TIME) {
            Log.d(TAG, "onActivityResult: Activity Result = course get time");
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: Activity Result result OK");
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", java.util.Locale.getDefault());

                time = (Time) data.getSerializableExtra("time");

                if (time.isBlockSchedule()) {
                    viewCourseTime.setText("A/B Schedule");
                } else if (time.isDaySchedule()) {
                    viewCourseTime.setText("Per Day Schedule");
                } else {
                    Toast.makeText(this, "An error occurred, please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewCourseTime.append("; " + formatter.format(new Date(time.getStartLong())) + " - " + formatter.format(new Date(time.getEndLong())));
                viewAddTime.setVisibility(View.GONE);
                viewCourseTime.setVisibility(View.VISIBLE);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                viewAddTime.setVisibility(View.VISIBLE);
                viewCourseTime.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Save course
        course = new Course();
        if (time == null) {
            Toast.makeText(this, "A time is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        if (viewCourseName.getText().toString().equals("")) {
            Toast.makeText(this, "A course name is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            course.setName(viewCourseName.getText().toString());
        }

        if (viewCourseTeacher.getText().toString().equals("")) {
            Toast.makeText(this, "A teacher is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            course.setTeacher(viewCourseTeacher.getText().toString());
        }

        course.setRoomNumber(viewCourseRoomNumber.getText().toString());
        course.setDescription(viewCourseDescription.getText().toString());
        course.setColor(courseColor);

        for (UserInfo info : Database.getUser().getProviderData()) {
            if (info.getProviderId().equals("google.com")) {
                if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)) {
                    signInGoogleCredential();
                } else {
                    EasyPermissions.requestPermissions(
                            this,
                            "This app needs to access your Google account (via Contacts).",
                            REQUEST_PERMISSION_GET_ACCOUNTS,
                            android.Manifest.permission.GET_ACCOUNTS);
                }
            }
        }

        new CreateCalEvent(gCredential).execute();
        return super.onOptionsItemSelected(item);
    }

    private int getMatColor(String typeColor) {
        int returnColor = Color.BLACK;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
        if (dialogTag.equals(COLOR_DIALOG)) {
            courseColor = extras.getInt(SimpleColorDialog.COLOR);
            viewColor.setBackgroundColor(courseColor);
            return true;
        }
        return false;
    }


    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void signInGoogleCredential() {
        gCredential = GoogleAccountCredential.usingOAuth2(
                this, Arrays.asList(LauncherLogin.SCOPES))
                .setBackOff(new ExponentialBackOff());
        gCredential.setSelectedAccountName(Database.getUser().getEmail());
    }



    private class CreateCalEvent extends AsyncTask<Void, Void, Time> {
        private com.google.api.services.classroom.Classroom mService = null;
        private Exception mLastError = null;

        CreateCalEvent(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            calService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, gCredential)
                    .setApplicationName("Unity Planner")
                    .build();
        }

        @Override
        protected Time doInBackground(Void... params) {

            // Getting user default timezone
            String timezone = TimeZone.getDefault().getID();

            SimpleDateFormat recurrenceFormatter = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());

            Event event = new Event();
            event.setSummary(course.getName());

            DateTime startDateTime = new DateTime(time.getStartLong());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone(timezone);
            event.setStart(start);

            DateTime stopDateTime = new DateTime(time.getEndLong());
            EventDateTime stop = new EventDateTime()
                    .setDateTime(stopDateTime)
                    .setTimeZone(timezone);
            event.setEnd(stop);

            if (time.isBlockSchedule()) {
                event.setRecurrence(Arrays.asList("RRULE:FREQ=DAILY;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR;UNTIL=" + recurrenceFormatter.format(new Date(time.getFinish()))));
            } else if (time.isDaySchedule()) {
                event.setRecurrence(Arrays.asList("RRULE:FREQ=WEEKLY;INTERVAL=1;BYDAY=" + time.getDays() + ";UNTIL=" + recurrenceFormatter.format(new Date(time.getFinish()))));
            }

            Event recurringEvent = null;

            try {
                time.setCalEvent(recurringEvent = calService.events().insert("primary", event).execute());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return time;
        }

        @Override
        protected void onPostExecute(Time time) {
            super.onPostExecute(time);
            course.setTime(time);
            try {
                Database.createCourse(course);
            } catch (DatabaseException e) {
                FirebaseCrash.log("Google Calendar v Firebase crash");
            }
            startActivity(new Intent(CreateCourse.this, Dashboard.class));
        }
    }
}
