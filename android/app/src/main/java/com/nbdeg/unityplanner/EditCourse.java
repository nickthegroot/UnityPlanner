package com.nbdeg.unityplanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nbdeg.unityplanner.data.Course;
import com.nbdeg.unityplanner.data.Time;
import com.nbdeg.unityplanner.utils.CourseAddTime;
import com.nbdeg.unityplanner.utils.Database;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditCourse extends AppCompatActivity implements SimpleDialog.OnDialogResultListener {

    private static final int COURSE_GET_TIME = 1050;
    private static final int REQUEST_CALENDAR = 1060;
    private Course newCourse;
    private Course oldCourse;
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

    final static private String[] perms = {android.Manifest.permission.READ_CALENDAR, android.Manifest.permission.WRITE_CALENDAR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", java.util.Locale.getDefault());

        Button colorSelector = (Button) findViewById(R.id.course_edit_button);
        Button deleteCourse = (Button) findViewById(R.id.course_edit_delete);
        viewAddTime = (Button) findViewById(R.id.course_edit_add_time);
        viewCourseTime = (TextView) findViewById(R.id.course_edit_time);
        viewColor = findViewById(R.id.course_edit_view_color);
        viewCourseName = (TextInputEditText) findViewById(R.id.course_edit_name);
        viewCourseTeacher = (TextInputEditText) findViewById(R.id.course_edit_teacher);
        viewCourseRoomNumber = (TextInputEditText) findViewById(R.id.course_edit_room_number);
        viewCourseDescription = (TextInputEditText) findViewById(R.id.course_edit_description);

        String ID = getIntent().getStringExtra("ID");
        for (Course course : Database.getCourses()) {
            if (course.getID().equals(ID)) {
                oldCourse = course;
                time = course.getTime();

                courseColor = course.getColor();
                viewColor.setBackgroundColor(courseColor);

                viewCourseName.setText(course.getName());
                viewCourseTeacher.setText(course.getTeacher());
                viewCourseRoomNumber.setText(course.getRoomNumber());
                viewCourseDescription.setText(course.getDescription());

                if (time.getStartLong() != null && time.getEndLong() != null && time.getFinish() != null ) {
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
                }
            }
        }

        deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                if (time.getCalEventID() != null) {
                                    deleteCalEvent();
                                } else {
                                    Database.deleteCourse(oldCourse);
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCourse.this);
                builder.setTitle("Are you sure you want delete " + oldCourse.getName() + "?")
                        .setMessage("Deleting this course will also delete all assignments attached to it.")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }
        });

        colorSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleColorDialog.build()
                        .title("Pick a course color")
                        .allowCustom(true)
                        .show(EditCourse.this, COLOR_DIALOG);
            }
        });

        viewCourseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCourse.this, CourseAddTime.class);
                startActivityForResult(intent, COURSE_GET_TIME);
            }
        });

        viewAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCourse.this, CourseAddTime.class);
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
        newCourse = new Course();
        newCourse.setClassroomCourse(oldCourse.getClassroomCourse());
        newCourse.setID(oldCourse.getID());

        if (time == null) {
            Toast.makeText(this, "A time is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        if (viewCourseName.getText().toString().equals("")) {
            Toast.makeText(this, "A course name is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            newCourse.setName(viewCourseName.getText().toString());
        }

        if (viewCourseTeacher.getText().toString().equals("")) {
            Toast.makeText(this, "A teacher is needed", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        } else {
            newCourse.setTeacher(viewCourseTeacher.getText().toString());
        }

        newCourse.setRoomNumber(viewCourseRoomNumber.getText().toString());
        newCourse.setDescription(viewCourseDescription.getText().toString());
        newCourse.setColor(courseColor);


        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("preference_sync_gcalendar", true) && newCourse.getTime() != null && newCourse.getTime().getEndLong() != null) {
            editCalEvent();
        } else {
            newCourse.setTime(time);
            Database.editCourse(newCourse, oldCourse);
            startActivity(new Intent(EditCourse.this, Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
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

    @AfterPermissionGranted(REQUEST_CALENDAR)
    private void deleteCalEvent() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            new DeleteCalEvent().execute();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your calendar.",
                    REQUEST_CALENDAR,
                    perms);
        }
    }

    @AfterPermissionGranted(REQUEST_CALENDAR)
    private void editCalEvent() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            new EditCalEvent().execute();
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



    private class EditCalEvent extends AsyncTask<Void, Void, Time> {

        @SuppressLint("MissingPermission")
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
            values.put(CalendarContract.Events.TITLE, newCourse.getName());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timezone);
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_COLOR, newCourse.getColor());
            if (time.isBlockSchedule()) {
                values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;INTERVAL=2;BYDAY=MO,TU,WE,TH,FR;UNTIL=" + recurrenceFormatter.format(new Date(time.getFinish())));
            } else if (time.isDaySchedule()) {
                values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;INTERVAL=1;BYDAY=" + time.getDays() + ";UNTIL=" + recurrenceFormatter.format(new Date(time.getFinish())));
            }

            if (ActivityCompat.checkSelfPermission(EditCourse.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                if (time.getCalEventID() != null) {
                    Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, time.getCalEventID());
                    cr.update(updateUri, values, null, null);       // Updates event
                } else {
                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                    time.setCalEventID(Long.parseLong(uri.getLastPathSegment()));
                }
            }

            return time;
        }

        @Override
        protected void onPostExecute(Time time) {
            super.onPostExecute(time);

            newCourse.setTime(time);
            Database.editCourse(newCourse, oldCourse);
            startActivity(new Intent(EditCourse.this, Dashboard.class));
        }
    }

    private class DeleteCalEvent extends AsyncTask<Void, Void, Void> {

        @SuppressLint("MissingPermission")
        @Override
        protected Void doInBackground(Void... params) {
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, time.getCalEventID());
            getContentResolver().delete(deleteUri, null, null); // Deletes event
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Database.deleteCourse(oldCourse);
            startActivity(new Intent(EditCourse.this, Dashboard.class));
        }
    }
}
