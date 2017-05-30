package com.nbdeg.unityplanner.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nbdeg.unityplanner.Data.Time;
import com.nbdeg.unityplanner.R;

import java.util.Calendar;

public class CourseAddTime extends AppCompatActivity {

    private EditTextDatePicker viewStartDate;
    private EditTextDatePicker viewStopDate;
    private EditTextTimePicker viewStartTime;
    private EditTextTimePicker viewStopTime;
    private RadioButton viewBlockSchedule;
    private RadioButton viewDaySchedule;

    private RadioButton viewADay;
    private RadioButton viewBDay;

    private CheckBox viewDayMon;
    private CheckBox viewDayTue;
    private CheckBox viewDayWen;
    private CheckBox viewDayThu;
    private CheckBox viewDayFri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_time);

        viewStartTime = new EditTextTimePicker((EditText) findViewById(R.id.course_add_time_start_time), this);
        viewStopTime = new EditTextTimePicker((EditText) findViewById(R.id.course_add_time_stop_time), this);

        viewStartDate = new EditTextDatePicker(this, R.id.course_add_time_start_date);
        viewStopDate = new EditTextDatePicker(this, R.id.course_add_time_stop_date);

        viewBlockSchedule = (RadioButton) findViewById(R.id.course_add_time_ab_schedule);
        viewDaySchedule = (RadioButton) findViewById(R.id.course_add_time_per_day_schedule);
        final RelativeLayout layoutBlockSchedule = (RelativeLayout) findViewById(R.id.course_add_time_ab_schedule_layout);
        final LinearLayout layoutDaySchedule = (LinearLayout) findViewById(R.id.course_add_time_per_day_schedule_layout);

        viewADay = (RadioButton) findViewById(R.id.course_add_time_ab_schedule_a_day);
        viewBDay = (RadioButton) findViewById(R.id.course_add_time_ab_schedule_b_day);

        viewDayMon = (CheckBox) findViewById(R.id.course_add_time_per_day_schedule_mon);
        viewDayTue = (CheckBox) findViewById(R.id.course_add_time_per_day_schedule_tue);
        viewDayWen = (CheckBox) findViewById(R.id.course_add_time_per_day_schedule_wen);
        viewDayThu = (CheckBox) findViewById(R.id.course_add_time_per_day_schedule_thu);
        viewDayFri = (CheckBox) findViewById(R.id.course_add_time_per_day_schedule_fri);

        viewBlockSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBlockSchedule.setChecked(true);
                viewDaySchedule.setChecked(false);
                layoutBlockSchedule.setVisibility(View.VISIBLE);
                layoutDaySchedule.setVisibility(View.GONE);
            }
        });

        viewDaySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBlockSchedule.setChecked(false);
                viewDaySchedule.setChecked(true);
                layoutBlockSchedule.setVisibility(View.GONE);
                layoutDaySchedule.setVisibility(View.VISIBLE);
            }
        });

        viewADay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewADay.setChecked(true);
                viewBDay.setChecked(false);
            }
        });

        viewBDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewADay.setChecked(false);
                viewBDay.setChecked(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewStartDate.date == null || viewStopDate.date == null) {
            Toast.makeText(this, "No time was added", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        Time courseTime = null;
        Calendar startCal = viewStartTime.cal;
        Calendar endCal = viewStopTime.cal;

        if (viewStartTime.cal.getTimeInMillis() > viewStopTime.cal.getTimeInMillis()) {
            Toast.makeText(this, "The starting time must be before the ending time!", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }


        if (viewBlockSchedule.isChecked()) {
            // Block Schedule

            courseTime = new Time(
                    true,
                    viewADay.isChecked(),
                    viewBDay.isChecked(),
                    false,
                    null,
                    startCal.getTimeInMillis(),
                    endCal.getTimeInMillis(),
                    viewStopDate.date.getTime()
            );

        } else if (viewDaySchedule.isChecked()) {
            // Day Schedule

            StringBuilder dayBuilder = new StringBuilder();
            if (viewDayMon.isChecked()) {
                dayBuilder.append("MON,");
            }
            if (viewDayTue.isChecked()) {
                dayBuilder.append("TUE,");
            }
            if (viewDayWen.isChecked()) {
                dayBuilder.append("WEN,");
            }
            if (viewDayThu.isChecked()) {
                dayBuilder.append("THU,");
            }
            if (viewDayFri.isChecked()) {
                dayBuilder.append("FRI,");
            }

            if (dayBuilder.toString().equals("")) {
                Toast.makeText(this, "Days are needed", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else {
                dayBuilder.deleteCharAt(dayBuilder.length()-1);
                dayBuilder.append(";");
            }

            courseTime = new Time(
                    false,
                    false,
                    false,
                    true,
                    dayBuilder.toString(),
                    startCal.getTimeInMillis(),
                    endCal.getTimeInMillis(),
                    viewStopDate.date.getTime()
            );
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("time", courseTime);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "No time was saved", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        super.onBackPressed();
    }
}