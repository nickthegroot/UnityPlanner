package com.nbdeg.unityplanner.data;

public class Assignments {
    private String dueDate;
    private String assignmentName;
    private String extraInfo;
    private String dueClass;
    private int percentComplete;

    // Empty Constructor
    public Assignments() {}

    // Adding info in one call
    public Assignments(String assignmentName, String dueClass, String dueDate, String extraInfo, int percentComplete) {
        this.assignmentName = assignmentName;
        this.dueClass = dueClass;
        this.dueDate = dueDate;
        this.extraInfo = extraInfo;
        this.percentComplete = percentComplete;
    }

    /* --- Setters --- */
    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }
    public void setAssignmentClass(String dueClass) {
        this.dueClass = dueClass;
    }
    public void setAssignmentDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public void setAssignmentExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
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
    public String getAssignmentDueDate() {
        return this.dueDate;
    }
    public int getAssignmentPercent() {
        return this.percentComplete;
    }
}

