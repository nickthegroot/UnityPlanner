package com.nbdeg.unityplanner.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;

import java.util.ArrayList;

public class Database {

    private static final String TAG = "Database";
    private static ArrayList<String> courseWorkIDs = new ArrayList<>();
    private static ArrayList<String> courseIDs = new ArrayList<>();

    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static DatabaseReference classDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("classes");
    public static DatabaseReference doneAssignmentsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments").child("done");
    public static DatabaseReference dueAssignmentsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments").child("due");
    public static DatabaseReference allAssignmentsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments").child("all");

    public static void refreshDatabase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        classDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("classes");
        doneAssignmentsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments").child("done");
        dueAssignmentsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments").child("due");
        allAssignmentsDb = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("assignments").child("all");
    }

    // Gets all classroom coursework
    public static ArrayList<String> getClassroomCourseWork() {
        courseWorkIDs.clear();
        allAssignmentsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    courseWorkIDs.add(assignment.getClassroomCourseWork().getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        return courseWorkIDs;
    }

    // Gets all classroom courses
    public static ArrayList<String> getClassroomCoursesId() {
        courseIDs.clear();
        classDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Classes mClass = userSnapshot.getValue(Classes.class);
                    courseIDs.add(mClass.getClassroomCourse().getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return courseIDs;
    }


    public static void createDueAssignment(Assignments assignment) {
        Log.i(TAG, "Creating due assignment: " + assignment.getAssignmentName());

        String key = dueAssignmentsDb.push().getKey();
        assignment.setID(key);
        allAssignmentsDb.child(key).setValue(assignment);
        dueAssignmentsDb.child(key).setValue(assignment);
    }

    public static void createFinishedAssignment(Assignments assignment) {
        Log.i(TAG, "Creating finished assignment: " + assignment.getAssignmentName());

        String key = doneAssignmentsDb.push().getKey();
        assignment.setID(key);
        allAssignmentsDb.child(key).setValue(assignment);
        doneAssignmentsDb.child(key).setValue(assignment);
    }

    public static DatabaseReference createClass(Classes mClass) {
        Log.i(TAG, "Creating class: " + mClass.getName());
        String key = classDb.push().getKey();
        mClass.setID(key);
        classDb.child(key).setValue(mClass);
        return classDb.child(key);
    }

    public static void editClass(final Classes newClass, final Classes oldClass) {
        classDb.child(oldClass.getID()).setValue(newClass);

        // Update All Assignments Under That Name
        allAssignmentsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    if (assignment.getDueClass().equalsIgnoreCase(oldClass.getName())) {
                        assignment.setDueClass(newClass.getName());
                        if (assignment.getPercentComplete() == 100) {
                            allAssignmentsDb.child(assignment.getID()).setValue(assignment);
                            doneAssignmentsDb.child(assignment.getID()).setValue(assignment);
                        } else {
                            allAssignmentsDb.child(assignment.getID()).setValue(assignment);
                            dueAssignmentsDb.child(assignment.getID()).setValue(assignment);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void editAssignment(final Assignments assignment, Boolean wasFinished) {
        if (wasFinished) {
            // Assignment used to be finished, check and see if it's not anymore.

            if (assignment.getPercentComplete() < 100) {

                // Assignment not finished anymore, update that in database.
                dueAssignmentsDb.child(assignment.getID()).setValue(assignment);
                allAssignmentsDb.child(assignment.getID()).setValue(assignment);
                doneAssignmentsDb.child(assignment.getID()).removeValue();
            } else {

                // Assignment still finished, just update new values in database.
                doneAssignmentsDb.child(assignment.getID()).setValue(assignment);
                allAssignmentsDb.child(assignment.getID()).setValue(assignment);
            }
        } else {
            // Assignment didn't used to be finished. Check if it is now.

            if (assignment.getPercentComplete() == 100) {

                // Assignment now finished, update that in database.
                dueAssignmentsDb.child(assignment.getID()).removeValue();
                allAssignmentsDb.child(assignment.getID()).setValue(assignment);
                doneAssignmentsDb.child(assignment.getID()).setValue(assignment);
            } else {
                // Assignment still due, just update new values in database.
                allAssignmentsDb.child(assignment.getID()).setValue(assignment);
                dueAssignmentsDb.child(assignment.getID()).setValue(assignment);
            }
        }
    }

    /*

    NOTIFICATIONS
    TODO Debug notifications.

    private static void cancelNotification(Context context, Assignments assignments) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(assignments.getNotificationIntent());
    }

    private static void editNotification(Context context, Assignments assignments) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(assignments.getNotificationIntent());
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, assignments.getDueDate(), assignments.getNotificationIntent());
    }

    private static void scheduleNotification(Notification notification, long notifyTime, Context context, Assignments assignment) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, assignment.getID());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        assignment.setNotificationIntent(pendingIntent);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notifyTime, pendingIntent);
    }

    private Notification getNotification(Assignments assignment, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Assignment Due Tomorrow");
        builder.setContentText(assignment.getName());
        builder.setSmallIcon(R.drawable.ic_assignments_due);
        return builder.build();
    }
    */
}
