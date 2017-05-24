package com.nbdeg.unityplanner.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.AssignmentViewer;
import com.nbdeg.unityplanner.Data.Assignment;
import com.nbdeg.unityplanner.R;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private int NOTIFICATION_ID = 803;

    @Override
    public void onReceive(final Context context, Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (prefs.getBoolean("notifications", true)) {
            // Notification for each due assignment

            Database.dueAssignmentDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                    final Notification.InboxStyle inboxStyle = new Notification.InboxStyle();

                    final int daysInAdvance = Integer.parseInt(prefs.getString("notifications_days", "1"));
                    final Calendar calCutOff = Calendar.getInstance();
                    calCutOff.setTimeInMillis(System.currentTimeMillis());
                    calCutOff.add(Calendar.DATE, daysInAdvance);
                    calCutOff.set(Calendar.HOUR_OF_DAY, 23);

                    final Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, -1);

                    int numberOfAssignments = 0;

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Assignment assignment = userSnapshot.getValue(Assignment.class);

                        if (assignment.getDueDate() < calCutOff.getTimeInMillis()) {
                            final Notification.Builder builder = new Notification.Builder(context);
                            numberOfAssignments++;

                            Intent notifyIntent = new Intent(context, AssignmentViewer.class);
                            notifyIntent.putExtra("ID", assignment.getID());
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            builder.setContentTitle(assignment.getName());
                            inboxStyle.addLine(assignment.getName());

                            if (android.os.Build.VERSION.SDK_INT >= 21) {
                                builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                builder.setGroup("Assignments");
                                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                            }

                            if (Build.VERSION.SDK_INT >= 23) {
                                Intent actionIntent = new Intent(context, AssignmentViewer.class);
                                actionIntent.putExtra("ID", assignment.getID());
                                actionIntent.putExtra("finish", true);
                                PendingIntent pendingActionIntent = PendingIntent.getActivity(context, NOTIFICATION_ID+100, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                Notification.Action actionDone = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_check_black), "Mark as finished", pendingActionIntent).build();
                                builder.addAction(actionDone);
                            }

                            if (assignment.getDueDate() < cal.getTimeInMillis()) {
                                builder.setContentText(
                                        "Assignment was due "
                                                + String.valueOf(daysBetween(new Date(assignment.getDueDate()), new Date())) +
                                                " days ago.");
                            } else if (String.valueOf(daysBetween(cal.getTime(), new Date(assignment.getDueDate()))).equals("0")) {
                                builder.setContentText("Assignment due today.");
                            } else if (String.valueOf(daysBetween(cal.getTime(), new Date(assignment.getDueDate()))).equals("1")) {
                                builder.setContentText("Assignment due in " + String.valueOf(-1 * daysBetween(new Date(assignment.getDueDate()), cal.getTime())) + " day.");
                            } else {
                                builder.setContentText("Assignment due in " + String.valueOf(-1 * daysBetween(new Date(assignment.getDueDate()), cal.getTime())) + " days.");
                            }

                            builder.setAutoCancel(true);
                            builder.setSmallIcon(R.drawable.ic_notification);
                            builder.setContentIntent(pendingIntent);
                            builder.setSound(Uri.parse(prefs.getString("notifications_ringtone", "content://settings/system/notification_sound")));
                            if (prefs.getBoolean("notifications_vibrate", true)) {
                                builder.setVibrate(new long[]{1000});
                            } else {
                                builder.setVibrate(new long[]{0});
                            }

                            managerCompat.notify(NOTIFICATION_ID, builder.build());
                            NOTIFICATION_ID++;
                        }
                    }

                    if (numberOfAssignments >= 1) {
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            inboxStyle.setBigContentTitle("Assignments Due Tomorrow");

                            Notification notif = new Notification.Builder(context)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_notification)
                                    .setStyle(inboxStyle)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                    .setGroup("Assignments")
                                    .setGroupSummary(true)
                                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                                    .build();

                            managerCompat.notify(NOTIFICATION_ID, notif);
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