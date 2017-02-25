package com.nbdeg.unityplanner.data;

@SuppressWarnings("unused")
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

