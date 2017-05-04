package com.nbdeg.unityplanner.Data;

import com.google.api.services.classroom.model.CourseWork;

public class Assignment {

    private String assignmentName;
    private Long dueDate;
    private String dueClass;
    private int percentComplete;
    private String extraInfo;
    private CourseWork classroomCourse;
    private String ID;

    /**
     * Empty Constructor
     */
    public Assignment() {}

    /**
     * Constructor for Google Classroom assignments
     * @see CourseWork
     * @param assignmentName Name of assignment
     * @param dueDate Due date of assignment stored in milliseconds
     * @param dueClass Name of due class
     * @param percentComplete Percent complete
     * @param extraInfo Any extra info stored
     * @param classroomCourse The Google Classroom stored class
     */
    public Assignment(String assignmentName, Long dueDate, String dueClass, int percentComplete, String extraInfo, CourseWork classroomCourse) {
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.dueClass = dueClass;
        this.percentComplete = percentComplete;
        this.extraInfo = extraInfo;
        this.classroomCourse = classroomCourse;
    }

    /* -- Getters and setters -- */

    public Long getDueDate() {
        return dueDate;
    }
    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return assignmentName;
    }
    public void setName(String assignmentName) {
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
