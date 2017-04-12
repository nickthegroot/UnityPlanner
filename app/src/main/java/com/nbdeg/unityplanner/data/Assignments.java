package com.nbdeg.unityplanner.data;

import com.google.api.services.classroom.model.CourseWork;

public class Assignments {
    private Long dueDate;
    private String assignmentName;
    private String extraInfo;
    private String dueClass;
    private int percentComplete;
    private CourseWork classroomCourse;
    private String ID;

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

    // Adding Classroom Assignment
    public Assignments(Long dueDate, String assignmentName, String extraInfo, String dueClass, int percentComplete, CourseWork classroomCourse) {
        this.dueDate = dueDate;
        this.assignmentName = assignmentName;
        this.extraInfo = extraInfo;
        this.dueClass = dueClass;
        this.percentComplete = percentComplete;
        this.classroomCourse = classroomCourse;
    }

    public Long getDueDate() {
        return dueDate;
    }
    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public String getAssignmentName() {
        return assignmentName;
    }
    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getDueClass() {
        return dueClass;
    }
    public void setDueClass(String dueClass) {
        this.dueClass = dueClass;
    }

    public int getPercentComplete() {
        return percentComplete;
    }
    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public CourseWork getClassroomCourseWork() {
        return classroomCourse;
    }
    public void setClassroomCourseWork(CourseWork classroomCourse) {
        this.classroomCourse = classroomCourse;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
}

