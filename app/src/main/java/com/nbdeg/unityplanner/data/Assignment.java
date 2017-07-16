package com.nbdeg.unityplanner.data;

import com.google.api.services.classroom.model.CourseWork;

public class Assignment {

    private String assignmentName;
    private Long dueDate;
    private Course dueCourse;
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
     * @param dueCourse The due course
     * @param percentComplete Percent complete
     * @param extraInfo Any extra info stored
     * @param classroomCourse The Google Classroom stored class
     */
    public Assignment(String assignmentName, Long dueDate, Course dueCourse, int percentComplete, String extraInfo, CourseWork classroomCourse) {
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.dueCourse = dueCourse;
        this.percentComplete = percentComplete;
        this.extraInfo = extraInfo;
        this.classroomCourse = classroomCourse;
    }

    public Assignment(Assignment assignment) {
        this.assignmentName = assignment.getName();
        this.dueDate = assignment.getDueDate();
        this.dueCourse = assignment.getDueCourse();
        this.percentComplete = assignment.getPercentComplete();
        this.extraInfo = assignment.getExtraInfo();
        this.classroomCourse = assignment.getClassroomCourseWork();
        this.ID = assignment.getID();
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

    public Course getDueCourse() {
        return dueCourse;
    }
    public void setDueCourse(Course dueCourse) {
        this.dueCourse = dueCourse;
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
