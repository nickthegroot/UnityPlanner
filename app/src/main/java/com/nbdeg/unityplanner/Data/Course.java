package com.nbdeg.unityplanner.Data;

public class Course {

    private String name;
    private String teacher;
    private Long startDate;
    private Long endDate;
    private String roomNumber;
    private String buildingName;
    private int color;
    private String ID;
    private com.google.api.services.classroom.model.Course classroomCourse;

    /**
     * Empty Constructor
     */
    public Course() {
    }


    /**
     * Constructor for Google Classroom assignments
     * @param name Name of class
     * @param teacher Full name of teacher
     * @param startDate Starting date of class in milliseconds
     * @param roomNumber Room number of classroom
     * @param classroomCourse Google Classroom course
     */
    public Course(String name, String teacher, Long startDate, String roomNumber, com.google.api.services.classroom.model.Course classroomCourse, int color) {
        this.name = name;
        this.teacher = teacher;
        this.startDate = startDate;
        this.roomNumber = roomNumber;
        this.classroomCourse = classroomCourse;
        this.color = color;
    }


    /* -- Getters and setters -- */

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Long getStartDate() {
        return startDate;
    }
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public com.google.api.services.classroom.model.Course getClassroomCourse() {
        return classroomCourse;
    }
    public void setClassroomCourse(com.google.api.services.classroom.model.Course classroomCourse) {
        this.classroomCourse = classroomCourse;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
}
