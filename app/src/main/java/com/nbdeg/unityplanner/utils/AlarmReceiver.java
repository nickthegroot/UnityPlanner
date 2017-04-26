package com.nbdeg.unityplanner.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.MainActivity;
import com.nbdeg.unityplanner.R;
import com.nbdeg.unityplanner.data.Assignments;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private int NOTIFICATION_ID = 803;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Notification for each due assignment
        Database.dueAssignmentsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numberOfAssignments = 0;
                Notification.Builder builder = new Notification.Builder(context);
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

                int daysInAdvance = Integer.parseInt(prefs.getString("notification_days_in_advance", "1"));

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.add(Calendar.DATE, daysInAdvance);
                cal.set(Calendar.HOUR_OF_DAY, 23);

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    if (assignment.getDueDate() < cal.getTimeInMillis()) {
                        // Assignment due next day, notify user about that
                        numberOfAssignments++;
                        inboxStyle.addLine(assignment.getAssignmentName());
                    }
                }

                if (numberOfAssignments != 0) {
                    builder.setSmallIcon(R.drawable.ic_logo);
                    builder.setAutoCancel(true);

                    //to be able to launch your activity from the notification
                    Intent notifyIntent = new Intent(context, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    builder.setSound(Uri.parse(prefs.getString("notifications_new_message_ringtone", "content://settings/system/notification_sound")));
                    if (prefs.getBoolean("notifications_new_message_vibrate", true)) {
                        builder.setVibrate(new long[]{1000});
                    } else {
                        builder.setVibrate(new long[]{0});
                    }

                    inboxStyle.setBigContentTitle(numberOfAssignments + " assignments due tomorrow");
                    inboxStyle.setSummaryText(numberOfAssignments + " assignments due tomorrow");
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
