package com.nbdeg.unityplanner.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class EditTextTimePicker implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    private EditText editText;
    public Calendar cal;
    private Context __context;

    private final SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", java.util.Locale.getDefault());

    public EditTextTimePicker(EditText editText, Context ctx){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.editText.setKeyListener(null);
        this.cal = Calendar.getInstance();

        __context = ctx;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            new TimePickerDialog(__context, this, hour, minute, false).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        this.editText.setText(formatter.format(cal.getTime()));
    }
}