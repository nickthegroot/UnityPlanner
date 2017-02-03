package com.nbdeg.unityplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Classes;

public class addClass extends AppCompatActivity {

    // TODO Add A/B Schedule
    // TODO Select which days class occurs on

    FirebaseUser user;
    DatabaseReference classDb;
    String TAG = "DB";

    EditText className;
    EditText classTeacher;
    EditText classStartDate;
    EditText classEndDate;
    EditText classRoomNumber;
    EditText classBuildingName;

    int roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        try {getActionBar().setTitle("Add a Class");}
        catch (NullPointerException e) {
            getSupportActionBar().setTitle("Add a Class");
        }

        // Setting date pickers to have have pop-up calendars
        new EditTextDatePicker(this, R.id.class_start_date);
        new EditTextDatePicker(this, R.id.class_end_date);

        // Setting EditText Variables
        className = (EditText) findViewById(R.id.class_name);
        classTeacher = (EditText) findViewById(R.id.class_teacher);
        classStartDate = (EditText) findViewById(R.id.class_start_date);
        classEndDate = (EditText) findViewById(R.id.class_end_date);
        classRoomNumber = (EditText) findViewById(R.id.class_room);
        classBuildingName = (EditText) findViewById(R.id.class_building);

        // Setting Firebase Variables
        user = FirebaseAuth.getInstance().getCurrentUser();
        classDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("classes");
    }

    // Adds a SAVE button to the Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = className.getText().toString();
        String teacherName = classTeacher.getText().toString();
        String startDate = classStartDate.getText().toString();
        String endDate = classEndDate.getText().toString();
        if (!classRoomNumber.getText().toString().isEmpty()) {
            roomNumber = Integer.parseInt(classRoomNumber.getText().toString());
        }
        String buildingName = classBuildingName.getText().toString();

        Classes newClass = new Classes(name, teacherName, startDate, endDate, roomNumber, buildingName);
        addClassToDatabase(newClass);
        return super.onOptionsItemSelected(item);
    }

    private void addClassToDatabase(Classes mClass) {
        Log.i(TAG, "Creating class: " + mClass.getClassName() + " in database");
        String key = classDb.push().getKey();
        classDb.child(key).setValue(mClass);

        startActivity(new Intent(addClass.this, classViewer.class));
    }
}
