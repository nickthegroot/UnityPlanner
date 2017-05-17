package com.nbdeg.unityplanner.Utils;

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
import com.nbdeg.unityplanner.AssignmentViewer;
import com.nbdeg.unityplanner.Data.Assignment;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private int NOTIFICATION_ID = 803;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (prefs.getBoolean("notifications", true)) {
            // Notification for each due assignment

            final Notification.Builder builder = new Notification.Builder(context);

            final int daysInAdvance = Integer.parseInt(prefs.getString("notifications_days", "1"));
            final Calendar calCutOff = Calendar.getInstance();
            calCutOff.setTimeInMillis(System.currentTimeMillis());
            calCutOff.add(Calendar.DATE, daysInAdvance);
            calCutOff.set(Calendar.HOUR_OF_DAY, 23);

            Database.dueAssignmentDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Assignment assignment = userSnapshot.getValue(Assignment.class);

                        if (assignment.getDueDate() < calCutOff.getTimeInMillis()) {

                            Intent notifyIntent = new Intent(context, AssignmentViewer.class);
                            notifyIntent.putExtra("ID", assignment.getID());

                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            builder.setContentTitle(assignment.getName());

                            if (assignment.getDueDate() < System.currentTimeMillis()) {
                                builder.setContentText("Assignment was due " + String.valueOf(daysBetween(new Date(assignment.getDueDate()), new Date())) + " days ago.");
                            } else {
                                builder.setContentText("Assignment due in " + String.valueOf(daysBetween(new Date(), new Date(assignment.getDueDate()))) + " days.");
                            }

                            builder.setAutoCancel(true);
                            builder.setContentIntent(pendingIntent);
                            builder.setSound(Uri.parse(prefs.getString("notifications_ringtone", "content://settings/system/notification_sound")));
                            if (prefs.getBoolean("notifications_vibrate", true)) {
                                builder.setVibrate(new long[]{1000});
                            } else {
                                builder.setVibrate(new long[]{0});
                            }

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                            managerCompat.notify(NOTIFICATION_ID, builder.build());
                            NOTIFICATION_ID++;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private long daysBetween(Date d1, Date d2){
        return ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}