package com.nbdeg.unityplanner.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.editAssignment;

public class AlarmReceiver extends BroadcastReceiver {
    private int NOTIFICATION_ID = 803;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Notification for each due assignment
        // TODO: Option to notify for each assignment or lump them together.
        Database.dueAssignmentsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numberOfAssignments = 0;
                Notification.Builder builder = new Notification.Builder(context);
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    // Assignment due next day, notify user about that
                    numberOfAssignments++;
                    inboxStyle.addLine(assignment.getAssignmentName());
                }

                if (numberOfAssignments != 0) {
                    builder.setSmallIcon(R.drawable.ic_logo);
                    builder.setAutoCancel(true);

                    //to be able to launch your activity from the notification
                    Intent notifyIntent = new Intent(context, editAssignment.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    inboxStyle.setBigContentTitle("Assignments Due Soon");
                    builder.setStyle(inboxStyle);

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
