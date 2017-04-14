package com.nbdeg.unityplanner.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.editAssignment;

public class AlarmReceiver extends BroadcastReceiver {
    private final int NOTIFICATION_ID = 803;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Notification for each due assignment
        // TODO: Option to notify for each assignment or lump them together.
        Database.dueAssignmentsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setContentTitle("Assignment Due Soon");
                    builder.setContentText(assignment.getAssignmentName());
                    builder.setSmallIcon(R.drawable.ic_logo);
                    builder.setAutoCancel(true);
                    Intent notifyIntent = new Intent(context, editAssignment.class);
                    notifyIntent.putExtra("ID", assignment.getID());
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //to be able to launch your activity from the notification
                    builder.setContentIntent(pendingIntent);

                    // Set extras if over API 23
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // only for marshmallow and newer versions (API 23)
                        Intent finishAssignmentIntent = new Intent(context, editAssignment.class);
                        finishAssignmentIntent.putExtra("CompleteExtra", true);
                        builder.addAction(new Notification.Action.Builder(null, "Complete Assignment", PendingIntent.getActivity(context, 3, finishAssignmentIntent, PendingIntent.FLAG_ONE_SHOT)).build());
                    }

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                    managerCompat.notify(NOTIFICATION_ID, builder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }
}
