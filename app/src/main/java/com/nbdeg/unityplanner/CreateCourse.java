package com.nbdeg.unityplanner;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.nbdeg.unityplanner.Data.Course;
import com.nbdeg.unityplanner.Data.Time;
import com.nbdeg.unityplanner.Utils.CourseAddTime;
import com.nbdeg.unityplanner.Utils.Database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateCourse extends AppCompatActivity implements SimpleDialog.OnDialogResultListener {

    private static final int REQUEST_CALENDAR = 1060;
    private static final int COURSE_GET_TIME = 1050;
    private static Course course;
    private Time time;

    private View viewColor;
    private Button viewAddTime;
    private TextView viewCourseTime;
    private TextInputEditText viewCourseName;
    private TextInputEditText viewCourseTeacher;
    private TextInputEditText viewCourseRoomNumber;
    private TextInputEditText viewCourseDescription;

    final static private String COLOR_DIALOG = "colorDialog";
    static private int courseColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        if (Database.getUser() == null) {
            startActivity(new Intent(CreateCourse.this, LauncherLogin.class));
        }

        Button colorSelector = (Button) findViewById(R.id.course_create_button);
        viewAddTime = (Button) findViewById(R.id.course_create_add_time);
        viewCourseTime = (TextView) findViewById(R.id.course_create_time);
        viewColor = findViewById(R.id.course_create_view_color);
        viewCourseName = (TextInputEditText) findViewById(R.id.course_create_name);
        viewCourseTeacher = (TextInputEditText) findViewById(R.id.course_create_teacher);
        viewCourseRoomNumber = (TextInputEditText) findViewById(R.id.course_create_room_number);
        viewCourseDescription = (TextInputEditText) findViewById(R.id.course_create_description);

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
        if (requestCode == COURSE_GET_TIME) {
            if (resultCode == Activity.RESULT_OK) {
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


        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("preference_sync_gcalendar", true)) {
            callCalEvent();
        } else {
            course.setTime(time);
            Database.createCourse(course);
            startActivity(new Intent(CreateCourse.this, Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @AfterPermissionGranted(REQUEST_CALENDAR)
    private void callCalEvent() {
        String[] perms = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
        if (EasyPermissions.hasPermissions(this, perms)) {
            new CreateCalEvent().execute();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your calendar.",
                    REQUEST_CALENDAR,
                    perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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


    private class CreateCalEvent extends AsyncTask<Void, Void, Time> {

        @Override
        protected Time doInBackground(Void... params) {

            // Getting user default timezone
            String timezone = TimeZone.getDefault().getID();

            SimpleDateFormat recurrenceFormatter = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();

            // Getting user cal
            long calID = 0;

            final String[] EVENT_PROJECTION = new String[] {
                    CalendarContract.Calendars._ID,                           // 0
            };

            // The indices for the projection array above.
            final int PROJECTION_ID_INDEX = 0;

            Cursor cur;
            Uri calUri = CalendarContract.Calendars.CONTENT_URI;
            String selection = "((" + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[] {Database.getUser().getEmail()};
            cur = cr.query(calUri, EVENT_PROJECTION, selection, selectionArgs, null);

            while (cur.moveToNext()) {
                calID = cur.getLong(PROJECTION_ID_INDEX);
                if (calID != 0) {
                    break;
                }
            }

            cur.close();

            // Setting event

            values.put(CalendarContract.Events.DTSTART, time.getStartLong());
            values.put(CalendarContract.Events.DTEND, time.getEndLong());
            values.put(CalendarContract.Events.TITLE, course.getName());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timezone);
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_COLOR, course.getColor());
            if (time.isBlockSchedule()) {
                values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR;UNTIL=" + recurrenceFormatter.format(new Date(time.getFinish())));
            } else if (time.isDaySchedule()) {
                values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;INTERVAL=1;BYDAY=" + time.getDays() + ";UNTIL=" + recurrenceFormatter.format(new Date(time.getFinish())));
            }

            if (ActivityCompat.checkSelfPermission(CreateCourse.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                time.setCalEventID(Long.parseLong(uri.getLastPathSegment()));
            }

            return time;
        }

        @Override
        protected void onPostExecute(Time time) {
            super.onPostExecute(time);
            course.setTime(time);

            Database.createCourse(course);
            FirebaseAnalytics.getInstance(getApplicationContext()).logEvent("course_created", null);
            startActivity(new Intent(CreateCourse.this, Dashboard.class));
        }
    }
}
