package com.nbdeg.unityplanner.data;

import android.app.PendingIntent;

@SuppressWarnings("unused")
public class Assignments {
    private Long dueDate;
    private String assignmentName;
    private String extraInfo;
    private String dueClass;
    private int percentComplete;
    private String ID;
    private PendingIntent notificationIntent;

    // Empty Constructor
    public Assignments() {}

    // Adding common info
    public Assignments(String assignmentName, String dueClass, Long dueDate, String extraInfo, int percentComplete) {
        this.assignmentName = assignmentName;
        this.dueClass = dueClass;
        this.dueDate = dueDate;
        this.extraInfo = extraInfo;
        this.percentComplete = percentComplete;
    }

    // Adding all info
    public Assignments(String assignmentName, String dueClass, Long dueDate, String extraInfo, int percentComplete, String ID) {
        this.assignmentName = assignmentName;
        this.dueClass = dueClass;
        this.dueDate = dueDate;
        this.extraInfo = extraInfo;
        this.percentComplete = percentComplete;
        this.ID = ID;
    }

    /* --- Setters --- */

    public void setName(String name) {
        this.assignmentName = name;
    }
    public void setClassName(String name) {
        this.dueClass = name;
    }
    public void setExtra(String extra) {
        this.extraInfo = extra;
    }
    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }
    public void setPercent(int percent) {
        this.percentComplete = percent;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public void setNotificationIntent(PendingIntent notificationIntent) {
        this.notificationIntent = notificationIntent;
    }

    /* --- Getters --- */

    public String getName() {
        return assignmentName;
    }
    public String getClassName() {
        return dueClass;
    }
    public String getExtra() {
        return extraInfo;
    }
    public Long getDueDate() {
        return dueDate;
    }
    public int getPercent() {
        return percentComplete;
    }
    public String getID() {
        return ID;
    }
    public PendingIntent getNotificationIntent() {
        return notificationIntent;
    }
}

