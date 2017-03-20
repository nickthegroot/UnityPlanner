package com.nbdeg.unityplanner.data;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Assignments {
    private Long dueDate;
    private String assignmentName;
    private String extraInfo;
    private String dueClass;
    private int percentComplete;

    // Empty Constructor
    public Assignments() {}

    // Adding info in one call
    public Assignments(String assignmentName, String dueClass, Long dueDate, String extraInfo, int percentComplete) {
        this.assignmentName = assignmentName;
        this.dueClass = dueClass;
        this.dueDate = dueDate;
        this.extraInfo = extraInfo;
        this.percentComplete = percentComplete;
    }

    /* --- Setters --- */

    public void setAssignmentName(String name) {
        this.assignmentName = name;
    }
    public void setAssignmentClassName(String name) {
        this.dueClass = name;
    }
    public void setAssignmentExtra(String extra) {
        this.extraInfo = extra;
    }
    public void setAssignmentDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }
    public void setAssignmentPercent(int percent) {
        this.percentComplete = percent;
    }

    /* --- Getters --- */

    public String getAssignmentName() {
        return this.assignmentName;
    }
    public String getAssignmentClassName() {
        return this.dueClass;
    }
    public String getAssignmentExtra() {
        return this.extraInfo;
    }
    public Long getAssignmentDueDate() {
        return this.dueDate;
    }
    public int getAssignmentPercent() {
        return this.percentComplete;
    }
}

