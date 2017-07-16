package com.nbdeg.unityplanner.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private final EditText _editText;
    private int _day;
    private int _month;
    private int _birthYear;
    private final Context _context;
    public Date date;

    public EditTextDatePicker(Context context, int editTextViewID)
    {
        Activity act = (Activity) context;
        this._editText = act.findViewById(editTextViewID);
        this._editText.setKeyListener(null);
        this._editText.setOnFocusChangeListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        String strDate = // Month is 0 based so add 1
                String.valueOf(_month + 1) + "/" + _day + "/" + _birthYear + " ";
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat niceFormatter = new SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault());
        _editText.setText(niceFormatter.format(date));
    }

    public void setDisplay(Date date) {
        this.date = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        _month = cal.get(Calendar.MONTH);
        _day = cal.get(Calendar.DAY_OF_MONTH);
        _birthYear = cal.get(Calendar.YEAR);
        updateDisplay();
    }
}
